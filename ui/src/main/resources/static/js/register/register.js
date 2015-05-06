(function () {
    var BASE_URL = "http://localhost:7070";
    var milked = angular.module('milked', []);

    milked.controller("RegistrationController", function ($http, $scope, $log) {
      $scope.registrationRequest = {};
      $scope.hasRegistered = false;
      $scope.hasRegisterErrors = false;
      $scope.hasLoginErrors = false;
      $scope.successfulLogin = false;

      $scope.registerUser = function() {
          $http.post(BASE_URL + "/registration", $scope.registrationRequest)
              .success(function (data, status, headers, config) {
                $log.info("Success: " + headers('Location'));
                $scope.hasRegistered = true;
              }).error(function(data, status, headers, config) {
                 $scope.hasRegisterErrors = true;
                 $log.info("Error: status =" + status + ", body =" + JSON.stringify(data));
              });
      }

    });
    })();