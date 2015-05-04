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

       milked.controller("GroupController", function ($http, $scope, $log, $rootScope) {
            $scope.groupRequest = {};
            $scope.groups = [];
            $scope.createGroup = function() {
                $http.post(BASE_URL + "/group", $scope.groupRequest).
                    success(function (data, status, headers, config) {
                      console.log("Success");
                      $rootScope.$broadcast("created-group");

                    }).error(function(data, status, headers, config) {
                       $log.info("Error: status = " + status + ", body = " + JSON.stringify(data));
                    });
            }

            $scope.getGroups = function() {
                $http.get(BASE_URL + "/group", $scope.groupRequest).
                    success(function (data, status, headers, config) {
                        $scope.groups = data;
                    }).error(function(data, status, headers, config) {
                       $log.info("Error: status = " + status + ", body = " + JSON.stringify(data));
                    });
            }

            $scope.$on("created-group", function(event) {
                $scope.getGroups();
            });
            $scope.getGroups();

        });

})();