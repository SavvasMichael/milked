(function () {
    var BASE_URL = "http://localhost:7070";
    var milked = angular.module('milked', []);

     milked.controller("UpdateController", function ($http, $scope, $log, $rootScope) {

     $scope.invitedUserDetails = {name:"", password:""};
     $scope.updateNewUserDetailsError = false;
     $scope.successfulUserUpdate = false;


        $scope.updateNewUserDetails = function() {
                    var uuid = $("#uuid").val();
                    $http.post(BASE_URL + "/user/" + uuid +"/update", $scope.invitedUserDetails)
                   .success(function (data, status, headers, config) {
                           $(".signupButton").attr("disabled", true);
                           console.log("success");
                           $scope.updateNewUserDetailsError = false;
                           $scope.successfulUserUpdate = true;
                   }).error(function(data, status, headers, config){
                           $scope.updateNewUserDetailsError = true;
                           $log.info("Error: status = " + status + ", body = " + JSON.stringify(data));
                   });
               }

               })
               })();