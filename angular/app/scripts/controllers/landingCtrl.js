/**
 * @ngdoc overview
 * @name pocsacApp
 * @description # pocsacApp
 *
 * Landing Controller.
 * Will be responsible for loading data for notifications and displaying a map component
 * This component will be re-used for admin functionality and end user
 * Header will have different options for an end user
 * Admin will be able to filter data and publish notifications
 */

'use strict';

cgiWebApp.controller('landingController',

  ['$scope','$filter','$timeout','EventNotificationService','uiGmapGoogleMapApi' , 'Localizator', '$geolocation', '$sessionStorage',
  function ($scope,$filter,$timeout,EventNotificationService, uiGmapGoogleMapApi, Localizator, $geolocation, $sessionStorage) {
    
  $scope.apiErrors = [];
  $scope.map = undefined;
  $scope.googleMaps = undefined;
  $scope.showMapOrDetails='MAP';
  $scope.activeItem = {item: -1};
  $scope.role = $sessionStorage.get('role');

  $scope.currentSelectedEvent=null;
    $scope.eventTypes = [
        { name: 'All', id: undefined},
        { name: 'Emergency', id: 'ADMIN_E'},
        { name: 'Non-Emergency', id: 'ADMIN_I'},
        { name: 'Weather', id: 'Weather'},
        { name: 'Flood', id: 'Flood'},
        { name: 'Fire', id: 'Fire'}];
    $scope.eventTimeFrames = [
        { name: '30 Days', id: 30},
        { name: '60 Days', id: 60},
        { name: '90 Days', id: 90},
        { name: '180 Days', id: 180},
        { name: '1 year', id: 365}];
    $scope.model = {
      notifications: []
    };
    $scope.eventTypeFilter=undefined;
    $scope.eventTimeFilter=30;
    $scope.changeFilters = function(){
        $scope.model.filteredNotifications = angular.copy( $scope.model.notifications); 
        $scope.model.filteredNotifications  =  $filter('filter')($scope.model.filteredNotifications, {type: $scope.eventTypeFilter}, true);
        $scope.model.filteredNotifications  =   $filter('eventTime')([$scope.model.filteredNotifications, $scope.eventTimeFilter]);
        $scope.currentSelectedEvent = $scope.model.filteredNotifications[0];
        $scope.activeItem.item = 0;
        $scope.loadMap($scope.model.filteredNotifications[0]);

    };
   uiGmapGoogleMapApi.then(function(maps) {
          $scope.maps =maps;
 
   }); 
 
   $scope.processApiErrors = function(response) {
    $scope.apiErrors = [];
    if (response.data && response.data.errors) {
      for (var i = 0; i < response.data.errors.length; i++) {
        if (response.data.errors[i].message) {
          $scope.apiErrors.push(response.data.errors[i].message);
        }
      }
    }
   };
    $scope.convertApiData = function(data){
        $scope.model.notifications = data ;
        // need to convert date string into a proper date.
        angular.forEach($scope.model.notifications,function(value){
           value.generationDate = Date.parse(value.generationDate); 
           if (value.geometry !== '' && value.geometry!==null && 
               value.geometry!== undefined){
                value.geometry = JSON.parse(value.geometry);
           }
        });
        $scope.changeFilters();
        
        
    };
 
    $scope.initLoad = function(){
        if ($scope.role === 'ADMIN'){
             EventNotificationService.allNotifications().then(function(response) {
                 $scope.convertApiData(response.data);
             }).catch(function(response) {
                        $scope.processApiErrors(response);
  
             });
        } else {
             EventNotificationService.userNotifications().then(function(response) {
                 $scope.convertApiData(response.data);
             }).catch(function(response) {
                    // omce implemented...this changes to report an error
                        $scope.processApiErrors(response);
  
             });
            
        }

        
    };
    
    $scope.loadEventDetails = function(selectedEvent, event){
       $scope.currentSelectedEvent = selectedEvent;
       $scope.showMapOrDetails='DETAILS';
       if(event){
         event.stopPropagation();
         event.preventDefault();
       }            
    };
    
    $scope.mapLoadASimplePoint = function (xValue, yValue) {        
                    // just a map point
                    var myLatLng = {lat: yValue, lng: xValue};
                    $scope.map.setCenter({lat: myLatLng.lat, lng: myLatLng.lng});
                    $scope.mapMarker = new $scope.maps.Marker({
                        position: myLatLng,
                        map: $scope.map
                    });
    };
    $scope.mapLoadFromRings = function (listOfRings) {
        var coordPoints = [];
        angular.forEach(listOfRings, function (ring) {
            angular.forEach(ring, function (ringEntry) {
                var singlePoint = {};
                singlePoint.lat = ringEntry[1];
                singlePoint.lng = ringEntry[0];
                coordPoints.push(singlePoint);
            });
        });
        /// take the first one in the list so we set the area of the map
        $scope.map.setCenter({lat: coordPoints[0].lat, lng: coordPoints[0].lng});

        // Construct the polygon.
        var ring = new $scope.maps.Polygon({
            paths: coordPoints,
            strokeColor: '#FF0000',
            strokeOpacity: 0.8,
            strokeWeight: 2,
            fillColor: '#FF0000',
            fillOpacity: 0.35
        });
        ring.setMap($scope.map);        
    };
    $scope.mapLoadFromForZipCodes = function (listOfZips) {
                     var geocoder = new $scope.maps.Geocoder();
                    var markers = [];
                    var bounds = new $scope.maps.LatLngBounds();
                    angular.forEach(listOfZips, function (zipCode) {
                        geocoder.geocode({'address': zipCode.zipCode}, function (results, status) {
                            if (status === 'OK') {
                                $scope.map.setCenter(results[0].geometry.location);
                                var marker = new $scope.maps.Marker({
                                    map: $scope.map,
                                    position: results[0].geometry.location
                                });
                                bounds.extend(results[0].geometry.location);
                                markers.push(marker);
                            }
                        });
                    });
                    $scope.map.fitBounds(bounds);      
    };
    
    //get the localization of the user and save it in his profile
    $scope.saveLocalization = function(){

        if($scope.isLoggedIn()){
            $geolocation.watchPosition({
                timeout: 60000,
                maximumAge: 250,
                enableHighAccuracy: true
            });
            
            if($geolocation.position.error === undefined || $geolocation.position.error === null){
                $scope.coords = $geolocation.position.coords;
                Localizator.localize($scope.coords).then(function(response) {
                    console.log(response.data);
                  });
            }else{
                console.log($geolocation.position.error);
                $scope.error = $geolocation.position.error;
            }
        }
    };

    
    $scope.loadMap = function (selectedEvent) {
        $scope.currentSelectedEvent = selectedEvent;
        $scope.showMapOrDetails = 'MAP';
        // the 2 variables above hide/show the map.
        // because of the way google mas renders, we need to make sure
        //that the map is set up AFTER the UI render. So we 
        // have this timeout whic forces the render to complete first.
        $timeout(function () {
            var ele = angular.element(document.querySelector('#map'));
            var mapIngfo = {center: {lat: -34.397, lng: 150.644}, zoom: 8};
            $scope.map = new $scope.maps.Map(ele[0], mapIngfo);
            if ($scope.currentSelectedEvent !== undefined) {
                if ($scope.currentSelectedEvent.geometry.rings !== undefined) {
                     $scope.mapLoadFromRings($scope.currentSelectedEvent.geometry.rings);
                } else if (selectedEvent.geometry.y !== undefined && selectedEvent.geometry.x !== undefined &&
                        selectedEvent.geometry.y !== '' && selectedEvent.geometry.x !== '') {
                    $scope.mapLoadASimplePoint(selectedEvent.geometry.x ,selectedEvent.geometry.y  );
                } else if (selectedEvent.eventNotificationZipcodes.length > 0) {
                    $scope.mapLoadFromForZipCodes(selectedEvent.eventNotificationZipcodes);
                }
            }
        }, 0, false);
    };
    $scope.initLoad();
    
    $scope.saveLocalization();

}]);
