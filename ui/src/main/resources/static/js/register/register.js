(function () {
    var BASE_URL = "http://milked.io";
//    var BASE_URL = "http://localhost:7070";
    var milked = angular.module('milked', []);

    milked.controller("RegistrationController", function ($http, $scope, $log) {
      $scope.registrationRequest = {};
      $scope.hasRegistered = false;
      $scope.hasRegisterErrors = false;
      $scope.hasLoginErrors = false;
      $scope.successfulLogin = false;
      $scope.registrationIsLoading = false;
      $scope.emailForRecovery = "";
      $scope.recoveryIsLoading = false;
      $scope.recoveryEmailSent = false;
      $scope.invalidRecoveryEmail = false;

      $scope.registerUser = function() {
        $('.signupButton').attr("disabled", true);
        $scope.registrationIsLoading = true;
          $http.post(BASE_URL + "/registration", $scope.registrationRequest)
              .success(function (data, status, headers, config) {
                $log.info("Success: " + headers('Location'));
                $scope.hasRegistered = true;
                $scope.registrationIsLoading = false;
                $('.signupButton').attr("disabled", false);
              }).error(function(data, status, headers, config) {
                 $scope.hasRegisterErrors = true;
                 $log.info("Error: status =" + status + ", body =" + JSON.stringify(data));
                 $scope.registrationIsLoading = false;
                $('.signupButton').attr("disabled", false);
              });
      }

      $scope.recoverPassword = function(){
        $scope.recoveryIsLoading = true;
        $('.passwordRecoveryButton').attr("disabled", true);
        $http.post(BASE_URL + "/user/forgot-password", {email: $scope.emailForRecovery})
            .success(function(data, status, headers, config) {
                $log.info("Success: " + headers("Location"));
                $scope.recoveryIsLoading = false;
                $('.passwordRecoveryButton').attr("disabled", false);
                $scope.recoveryEmailSent = true;
                $scope.invalidRecoveryEmail = false;
            }).error(function(data, status, headers, config) {
                $log.info("Error: status = " + status + ", body = " + JSON.stringify(data));
                $scope.recoveryIsLoading = false;
                $scope.invalidRecoveryEmail = true;
                $scope.recoveryEmailSent = false;
                $('.passwordRecoveryButton').attr("disabled", false);
                });
            }
    });
    })();