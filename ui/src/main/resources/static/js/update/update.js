(function () {
    var BASE_URL = "http://localhost:7070";
    var milked = angular.module('milked', []);

     milked.controller("UpdateController", function ($http, $scope, $log, $rootScope) {

     $scope.invitedUserDetails = {name:"", password:""};

        $scope.updateNewUserDetails = function() {
                    var uuid = $("#uuid").val();
                    $http.post(BASE_URL + "/user/" + uuid +"/update", $scope.invitedUserDetails)
                   .success(function (data, status, headers, config) {
                           console.log("success");
                   }).error(function(data, status, headers, config){
                           $log.info("Error: status = " + status + ", body = " + JSON.stringify(data));
                   });
               }

               })
               })();