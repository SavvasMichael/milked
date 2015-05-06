(function () {
    var BASE_URL = "http://localhost:7070";
    var milked = angular.module('milked', []);
milked.controller("LoginController", function ($http, $scope, $log) {
      $scope.loginRequest = {};
      $scope.login = function() {
          $http.post(BASE_URL + "/login", $scope.loginRequest).
              success(function (data, status, headers, config) {
                console.log("Success");
                $scope.successfulLogin = true;
              }).error(function(data, status, headers, config) {
                $scope.hasLoginErrors = true;
                 $log.info("Error: status = " + status + ", body = " + JSON.stringify(data));
              });
      }

      });
      })();