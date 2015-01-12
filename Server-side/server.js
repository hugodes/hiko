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
 

 mongoose.connect('mongodb://localhost/hikoTestDb', function(err) {
		  if (err) { throw err; }
	    });
// Creating Schema for an hike
var hikeSchema = new mongoose.Schema({
  _id :  Number,
  name: String,
  // used get & set to conversion to be able to store float number
  totalDistance: {type: Number, get: getConversion, set: setConversion },
  totalTime: {type: Number, get: getConversion, set: setConversion} 

});

var locationSchema = new mongoose.Schema({
	_id : Number,
	hikeId : Number,
	latitude : {type: Number, get: getConversion, set: setConversion },
	longitude : {type: Number, get: getConversion, set: setConversion }

});


function getConversion(num){
    return (num/100).toFixed(2);
}

function setConversion(num){
    return num*100;
}


 
// Creating model for an hike
var hikeModel = mongoose.model('hikes', hikeSchema);

// Creating model for a location
var locationModel = mongoose.model('locations', locationSchema);
																																				
/********************************************************
* USED FOR TEST ONLY
*********************************************************/
/*	
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
/*

var query = hikeModel.find(null);
query.exec(function (err, hikes) {
  if (err) { console.log(err); }
  console.log(hikes);
  mongoose.connection.close();
});



/********************************************************
* MODELS USED TO SEND DATA
*********************************************************/
function HikeToSend(id,name,totalDistance,totalTime){
	this.id = id;
	this.name = name;
	this.totalDistance = totalDistance;
	this.totalTime = totalTime;
}

function LocationToSend(id,hikeId,longitude,latitude){
	this.id = id;
	this.hikeId = hikeId;
	this.longitude = longitude;
	this.latitude = latitude;
}
/*
var hikeToAdd = new hikeModel();
		hikeToAdd._id = 1;
		hikeToAdd.name = "First hike";
		hikeToAdd.totalDistance = 2;
		hikeToAdd.totalTime = 3;
		hikeToAdd.save(function (err) {
			if (err) { throw err; }
			console.log('Hike added with succes!');
			// We close the connection from the database
	//		mongoose.connection.close();

		});

var locationToAdd = new locationModel();
locationToAdd._id = 1;
locationToAdd.hikeId = 1;
locationToAdd.longitude = 5;
locationToAdd.latitude = 4;
locationToAdd.save(function (err){
	if (err) {
		throw err;
	}
	console.log("Location added with succes");
});

var locationToAdd = new locationModel();
locationToAdd._id = 2;
locationToAdd.hikeId = 1;
locationToAdd.longitude = 3;
locationToAdd.latitude = 5;
locationToAdd.save(function (err){
	if (err) {
		throw err;
	}
	console.log("Location added with succes");
});*/
/********************************************************
* MANAGE IO CONNECTION
*********************************************************/
//Push
io.on('connection', function(socket){
  console.log('a user connected');

  
  	socket.on('set_db', function (data){
  		var queryLocation = locationModel.find(null);
     queryLocation.exec(function (err, locations){
     	if(err) {
     		console.log(err);
     	}
     	if(locations != null){
	     	for(var i=0;i<locations.length;i++){
	     		var locationToSend = new LocationToSend(locations[i].id,locations[i].hikeId,locations[i].longitude,locations[i].latitude);
	     		socket.emit('get_location', locationToSend);
	     		console.log(locations[i]);
	     	}
     	}
     });
	 var query = hikeModel.find(null);
	query.exec(function (err, hikes) {
	 if (err) { console.log(err); }
	 
	// console.log(hikes);
		if (hikes != null){
			for (var i=0;i<hikes.length;i++)
			{
			/*	var hikeToSend ={
					id: hikes[i].id,
					name: hikes[i].name,
					totalDistance: hikes[i].totalDistance,
					totalTime: hikes[i].totalTime
				};*/
				var hikeToSend = new HikeToSend(hikes[i].id,hikes[i].name,hikes[i].totalDistance,hikes[i].totalTime);
				socket.emit('get_db',hikeToSend);
				console.log(hikes[i]);
			}
		}
	 
	});

  	});
     
 // socket.emit('news', { hello: 'world' });	


  socket.on('add_location', function (data){
  
  	var locationToAdd = new locationModel();
  	locationToAdd._id = data.id;
  	locationToAdd.hikeId = data.hikeId;
  	locationToAdd.longitude = data.longitude;
  	locationToAdd.latitutde = data.latitude;
  	locationToAdd.save(function (err ) {
  		if (err ) { throw err; }
  		console.log(' location added with succes');
  	//	mongoose.connection.close();
  	});


  });



  //listener for the event add_db
  socket.on('add_db', function (data) {
  		
  		var hikeToAdd = new hikeModel();
		hikeToAdd._id = data.id;
		hikeToAdd.name = data.name;
		hikeToAdd.totalDistance = data.totalDistance;
		hikeToAdd.totalTime = data.totalTime;
		hikeToAdd.save(function (err) {
			if (err) { throw err; }
			console.log('Hike added with succes!');
			// We close the connection from the database
	//		mongoose.connection.close();

		});
          
  });


});

