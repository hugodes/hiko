/****************************************************************
* Name : server.js
* Authors : Théo Chartier & Hugo des Longchamps
* Date : 08.01.2015
* Description : Server in nodeJS wich communicate with android client through socket.IO
*
*****************************************************************/

var express = require('express');
var app = express();
var server = app.listen(3000);
var io = require('socket.io').listen(server);
var mongoose = require('mongoose');


/********************************************************
* DATABASE PART
*********************************************************/
 
// We are connecting to the database
// Dont forget to launch mongod in the terminal
mongoose.connect('mongodb://localhost/hikoTestDb', function(err) {
  if (err) { throw err; }
});
 
// Creating Schema for an hike
var hikeSchema = new mongoose.Schema({
  _id :  Number,
  name: String,
  // used get & set to conversion to be able to store float number
  totalDistance: {type: Number, get: getTotalDistance, set: setTotalDistance },
  totalTime: {type: Number, get: getTotalTime, set: setTotalTime} 

});


function getTotalDistance(num){
    return (num/100).toFixed(2);
}

function setTotalDistance(num){
    return num*100;
}


function getTotalTime(num){
    return (num/100).toFixed(2);
}

function setTotalTime(num){
    return num*100;
}
 
// Creating model for an hike
var hikeModel = mongoose.model('hikes', hikeSchema);


/********************************************************
* USED FOR TEST ONLY
*********************************************************/
	
// Creating example , for testing comment/uncomment Creating / Displaying (one action at the same time)
var newHike = new hikeModel({ _id : 1 });
newHike.name = 'First Hike';
newHike.totalDistance = 23.45;
newHike.totalTime = 20.56;
 
 /*
// We saving in the mongoDB db
newHike.save(function (err) {
  if (err) { throw err; }
  console.log('Hike ajouté avec succès !');
  // We close the connection from the database
  mongoose.connection.close();

});
*/


var query = hikeModel.find(null);
query.exec(function (err, hikes) {
  if (err) { console.log(err); }
  console.log(hikes);
  mongoose.connection.close();
});


/********************************************************
* MANAGE IO CONNECTION
*********************************************************/
//Push
io.on('connection', function(socket){
  console.log('a user connected');



  socket.emit('news', { hello: 'world' });

  //listener for the event add_db
  socket.on('add_db', function (data) {
          console.log(data);
  });


});

