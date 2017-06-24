'use strict';

const functions = require('firebase-functions');
const request = require("request");
const admin = require('firebase-admin');

admin.initializeApp(functions.config().firebase);

exports.login = functions.https.onRequest((req, res) => {
    var intCategories = {},
        userObj = {},
        categories = null;
    if (req.query.hasOwnProperty('categories') && req.query.categories != "") {
        console.log(req.query.categories);
        categories = JSON.parse(req.query.categories);
        if (categories instanceof Array) {
            for (var i = 0; i < categories.length; i++) {
                intCategories[categories[i]] = categories[i];
            }
        }
    }
    userObj[req.query.uid] = {
        uid: req.query.uid,
        name: req.query.name,
        isAdmin: JSON.parse(req.query.is_admin),
        mobileNo: req.query.mobile_no,
        isAvailable: true,
        categories: intCategories,
        pending: {},
        accepted: {},
        declined: {},
        complaints: {},
        lastLocationLat: null,
        lastLocationLong: null,
    }

    admin.database().ref('/users').update(userObj);
    res.write('User created');
    res.end();
});


exports.triggerUserChange = functions.database.ref('/users/{userId}').onWrite(event => {
    var userObj = event.data.val(),
        userId = userObj.uid,
        updateObj = {};

    updateObj[userId] = userId;

    var isAvailableSnapshot = event.data.child('isAvailable');
    if (isAvailableSnapshot.changed()) {
        if (userObj.isAdmin && isAvailableSnapshot.val()) {
            for (var category in userObj.categories) {
                console.log(category);
                admin.database().ref('/availableUsers/' + category).update(updateObj);
            }
        } else if (userObj.isAdmin && !isAvailableSnapshot.val()) {
            for (var category in userObj.categories) {
                console.log(category);
                admin.database().ref('/availableUsers/' + category + '/' + userId).remove();
            }
        }
    }

    return 0;
});


exports.addComplaint = functions.database.ref('/complaints/{complaintId}').onWrite(event => {
    var complaintObj = event.data.val(),
        updateObj = {};

    var category = complaintObj.category,
        locationLat = complaintObj.locationLat,
        locationLong = complaintObj.locationLong;

    complaintObj['complaintId'] = event.params.complaintId;
    updateObj[event.params.complaintId] = {
        complaintId: event.params.complaintId
    };

    var statusSnapshot = event.data.child('status');
    console.log(statusSnapshot.val());

    if (statusSnapshot.val() == null) {
        complaintObj['status'] = 'pending';
        admin.database().ref('/complaints/' + event.params.complaintId).update(complaintObj);
    } else if (statusSnapshot.val() == 'pending') {
        console.log('entered here!');
        admin.database().ref('availableUsers/' + category).once("value").then(function (snapshot) {
            console.log(snapshot.val());
        });
    }

    admin.database().ref('/users/' + complaintObj.user + '/complaints').update(updateObj);

    return 0;

});


exports.acceptComplaint = functions.database.ref('/users/{userId}/accepted/{complaintId}').onWrite(event => {
    var userId = event.params.userId,
        complaintId = event.params.complaintId;

    admin.database().ref('/complaints' + complaintId.toString()).update({
        'admin': userId
    });
});


// api = "https://maps.googleapis.com/maps/api/distancematrix/json?units=imperial&origins=28.5930586,77.2002744&destinations=28.4570952,77.085111&key=AIzaSyBPaiBY8ct-I4O0dGl4i3sWEgf-uK7c3g0"

// request.get(api, {}, function(err, res, body) {
//     res.write(body);
//     res.end()
// });
// res.write('Hello');
// res.end();
