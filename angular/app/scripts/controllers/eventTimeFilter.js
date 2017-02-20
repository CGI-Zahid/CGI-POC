/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
'use strict';


cgiWebApp.filter('eventTime', function ( ) {
   return function (args) {
       var data = args[0];
       var filterValue = args[1];
       var filteredData = [];
       var todaysDate = new Date();
       var compareDate = new Date();
       
        switch (filterValue)
        {
            case '1':
                  compareDate.setDate(todaysDate.getDate() - 30);
                break;
            case '2':
                  compareDate.setDate(todaysDate.getDate() - 60);
                break;
            case '3':
                  compareDate.setDate(todaysDate.getDate() - 90);
                break;
            case '4':
                  compareDate.setDate(todaysDate.getDate() - 180);
                break;
            case '5':
                  compareDate.setDate(todaysDate.getDate() - 365);
                break;
            
       }
                    
            angular.forEach(data,function(value){
            // we want to return the parent and all children for any gsn entry 
            // that matches.
            if (value.generationDate >= compareDate) {
               filteredData.push(value);   
                
            }
        });

       return filteredData;
    };
});