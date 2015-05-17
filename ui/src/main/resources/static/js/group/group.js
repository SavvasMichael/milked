(function () {
    var BASE_URL = "http://localhost:7070";
    var milked = angular.module('milked', []);

     milked.controller("GroupController", function ($http, $scope, $log, $rootScope) {

                $scope.currentGroupId = -1;
                $scope.groupDetails = [];
                $scope.groupRequest = {};
                $scope.groups = [];
                $scope.emailBody = {email:""};
                $scope.invitedUserDetails = {};

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

                $scope.getGroupDetails = function(groupId) {
                                $http.get(BASE_URL + "/group/" +groupId +"/users").
                                    success(function (data, status, headers, config) {
                                    if(data == null){
                                        console.log("Data is null");
                                    }
                                        $scope.groupDetails = data;
                                        $scope.currentGroupId = groupId;
                                        console.log(data);
                                    }).error(function(data, status, headers, config) {
                                        $log.info("Error: status = " + status + ", body = " + JSON.stringify(data));
                                    });
                            }

                $scope.leaveGroup = function(group) {
                                $http.post(BASE_URL + "/group/" + group.id + "/leave")
                                .success(function (data, status, headers, config) {
                                        console.log(data);
                                }).error(function(data, status, headers, config){
                                        $log.info("Error: status = " + status + ", body = " + JSON.stringify(data));
                                });
                }

                $scope.inviteUser = function() {
                               $http.post(BASE_URL + "/group/" + $scope.currentGroupId + "/invite/", $scope.emailBody)
                               .success(function (data, status, headers, config) {
                                       console.log("Success");
                               }).error(function(data, status, headers, config){
                                       $log.info("Error: status = " + status + ", body = " + JSON.stringify(data));
                               });
               }

               $scope.updateUserDetails = function() {
                                $http.post(BASE_URL + "/user/update/", $scope.invitedUserDetails)
                               .success(function (data, status, headers, config) {
                                       console.log("Success");
                               }).error(function(data, status, headers, config){
                                       $log.info("Error: status = " + status + ", body = " + JSON.stringify(data));
                               });
               }
                $scope.getGroups();
            });
    })();