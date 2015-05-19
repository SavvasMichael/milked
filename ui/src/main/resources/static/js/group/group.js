(function () {
    var BASE_URL = "http://localhost:7070";
    var milked = angular.module('milked', []);

     milked.controller("GroupController", function ($http, $scope, $log, $rootScope) {

                $scope.currentGroupId = -1;
                $scope.groupDetails = [];
                $scope.groupRequest = {};
                $scope.groups = [];
                $scope.emailBody = {email:""};
                $scope.invitedUserDetails = {name:"", password:""};
                $scope.milkingTransactions = [];
                $scope.milkingTransactionRequest = {};
                $scope.milkingUserId = 0;
                $scope.milkedUserId = 0;
                $scope.hasGroupId = false;
                $scope.selfMilk = false;

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
                                    $rootScope.$broadcast("loaded-group");
                                    $scope.hasGroupId = true;
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
                                if(confirm("Are you sure you want to leave the group " + group.name + "?")){
                                $http.post(BASE_URL + "/group/" + group.id + "/leave")
                                .success(function (data, status, headers, config) {
                                        console.log(data);
                                }).error(function(data, status, headers, config){
                                        $log.info("Error: status = " + status + ", body = " + JSON.stringify(data));
                                });
                                }
                }

                $scope.inviteUser = function() {
                               $http.post(BASE_URL + "/group/" + $scope.currentGroupId + "/invite/", $scope.emailBody)
                               .success(function (data, status, headers, config) {
                                       console.log("Success");
                               }).error(function(data, status, headers, config){
                                       $log.info("Error: status = " + status + ", body = " + JSON.stringify(data));
                               });
               }

               $scope.updateNewUserDetails = function() {
                                var uuid = $("#uuid").val();
                                $http.post(BASE_URL + "/user/" + uuid +"/update", $scope.invitedUserDetails)
                               .success(function (data, status, headers, config) {
                                       console.log("success");
                               }).error(function(data, status, headers, config){
                                       $log.info("Error: status = " + status + ", body = " + JSON.stringify(data));
                               });
               }

               $scope.milk = function(userId){
                               $scope.milkingTransactionRequest.milkeeId = userId;
                               $http.post(BASE_URL + "/group/" + $scope.currentGroupId + "/milk", $scope.milkingTransactionRequest)
                              .success(function (data, status, headers, config) {
                                      console.log("Success");
                              }).error(function(data, status, headers, config){
                                      $log.info("Error: status = " + status + ", body = " + JSON.stringify(data));
                                      $scope.selfMilk = true;
                              });
                             }
               $scope.milked = function(userId){
                              $scope.milkingTransactionRequest.milkerId = userId;
                              $http.post(BASE_URL + "/group/" + $scope.currentGroupId + "/milked", $scope.milkingTransactionRequest)
                             .success(function (data, status, headers, config) {
                                     console.log("Success");
                             }).error(function(data, status, headers, config){
                                     $log.info("Error: status = " + status + ", body = " + JSON.stringify(data));
                                     $scope.selfMilk = true;
                             });
                            }

               $scope.getMilkingTransactions = function(){
                            $http.get(BASE_URL + "/group/" + $scope.currentGroupId + "/milk")
                            .success(function (data, status, headers, config) {
                                    $scope.milkingTransactions = data;
                                    console.log("Success");
                            }).error(function(data, status, headers, config){
                                    $log.info("Error: status = " + status + ", body = " + JSON.stringify(data));
                            });
               }
               $scope.$on("loaded-group", function(event) {
                                    $scope.getMilkingTransactions();
                               });
                $scope.getGroups();
            });
    })();