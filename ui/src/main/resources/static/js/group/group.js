(function () {
    var BASE_URL = "http://milked.io";
//    var BASE_URL = "http://localhost:7070";
    var milked = angular.module('milked', ['angularUtils.directives.dirPagination']);

     milked.controller("GroupController", function ($http, $scope, $log, $rootScope) {
                $scope.justLanded = true;
                $scope.currentGroupId = -1;
                $scope.currentGroup = {};
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
                $scope.transactionOk = false;
                $scope.successfulUserInvite = false;
                $scope.unsuccessfulUserInvite = false;
                $scope.isLoading = false;
                $scope.invalidGroupName = false;
                $scope.moreThan0 = false;
                $scope.lessThan0 = false;
                $scope.fetchedHistory = false;

                $scope.toReadableTime = function(timestamp){
                    $scope.readableTime = new Date(timestamp);
                    return $scope.readableTime.getDate() + '/' + $scope.readableTime.getMonth() + '/' + $scope.readableTime.getFullYear();
                }
                $scope.toDecimal = function(amount){
                    var finalAmount = amount/100;
                    return finalAmount.toFixed(2);
                }
                $scope.removeNegative = function(amount){
                    var original = amount + "";
                    var positiveAmount = "";
                    for (var i = 1; i < original.length; i++){
                        positiveAmount += original.charAt(i);
                    }
                    return positiveAmount;
                }

                $scope.createGroup = function() {
                    $http.post(BASE_URL + "/group", $scope.groupRequest).
                        success(function (data, status, headers, config) {
                          $rootScope.$broadcast("created-group");
                          $scope.invalidGroupName = false;
                          $(".createGroupInput").val("");
                        }).error(function(data, status, headers, config) {
                          $scope.invalidGroupName = true;
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
                $scope.getGroupDetails = function(group) {
                  $http.get(BASE_URL + "/group/" + group.id +"/users").
                    success(function (data, status, headers, config) {
                        $scope.justLanded = false;
                        $scope.hasGroupId = true;
                        $scope.groupDetails = data;
                        $scope.currentGroupId = group.id;
                        $scope.currentGroup = group;
                    if($scope.groupDetails.loggedInUser.balance >= 0){
                        $scope.moreThan0 = true;
                        $scope.lessThan0 = false;
                    }
                    if($scope.groupDetails.loggedInUser.balance < 0){
                        $scope.lessThan0 = true;
                        $scope.moreThan0 = false;
                    }
                        $scope.getMilkingTransactions(group.id);
                    }).error(function(data, status, headers, config) {
                        $log.info("Error: status = " + status + ", body = " + JSON.stringify(data));
                    });
                        }

                  $scope.getGroupDetailsAfterMilk = function(currentGroupId, group) {
                    $http.get(BASE_URL + "/group/" + currentGroupId +"/users").
                    success(function (data, status, headers, config) {
                    $scope.hasGroupId = true;
                        $scope.groupDetails = data;
                        $scope.currentGroupId = $scope.currentGroupId;
                        $scope.currentGroup = group;
                    if($scope.groupDetails.loggedInUser.balance >= 0){
                        $scope.moreThan0 = true;
                        $scope.lessThan0 = false;
                    }
                    if($scope.groupDetails.loggedInUser.balance < 0){
                        $scope.lessThan0 = true;
                        $scope.moreThan0 = false;
                    }
                            $scope.getMilkingTransactions(group.id);
                    }).error(function(data, status, headers, config) {
                        $log.info("Error: status = " + status + ", body = " + JSON.stringify(data));
                    });
                        }


                $scope.leaveGroup = function(group) {
                    if(confirm("Are you sure you want to leave the group " + group.name + "?")){
                    $http.post(BASE_URL + "/group/" + group.id + "/leave")
                    .success(function (data, status, headers, config) {
                    }).error(function(data, status, headers, config){
                            $log.info("Error: status = " + status + ", body = " + JSON.stringify(data));
                    });
                    }
                }

                $scope.inviteUser = function() {
               $('.inviteButton').attr("disabled", true);
               $scope.isLoading = true;
               $http.post(BASE_URL + "/group/" + $scope.currentGroupId + "/invite/", $scope.emailBody)
               .success(function (data, status, headers, config) {
                       $scope.successfulUserInvite = true;
                       $scope.unsuccessfulUserInvite = false;
                       $('.inviteInput').val('')
                       $('.inviteButton').attr("disabled", false);
                       $scope.isLoading = false;
               }).error(function(data, status, headers, config){
                       $log.info("Error: status = " + status + ", body = " + JSON.stringify(data));
                       $scope.unsuccessfulUserInvite = true;
                       $scope.successfulUserInvite = false;
                       $('.inviteInput').val('')
                       $('.inviteButton').attr("disabled", false);
                       $scope.isLoading = false;
               });
               }

               $scope.milk = function(userId){
               $scope.milkingTransactionRequest.milkeeId = userId;
               $http.post(BASE_URL + "/group/" + $scope.currentGroupId + "/milk", $scope.milkingTransactionRequest)
              .success(function (data, status, headers, config) {
                      $scope.transactionOk = true;
                      $scope.selfMilk = false;
                      $rootScope.$broadcast("successful-transaction");
              }).error(function(data, status, headers, config){
                      $log.info("Error: status = " + status + ", body = " + JSON.stringify(data));
                      $scope.selfMilk = true;
                      $scope.transactionOk = false;
              });
             }
               $scope.milked = function(userId){
               $scope.milkingTransactionRequest.milkerId = userId;
               $http.post(BASE_URL + "/group/" + $scope.currentGroupId + "/milked", $scope.milkingTransactionRequest)
               .success(function (data, status, headers, config) {
                     $scope.transactionOk = true;
                     $scope.selfMilk = false;
                     $rootScope.$broadcast("successful-transaction");
             }).error(function(data, status, headers, config){
                     $log.info("Error: status = " + status + ", body = " + JSON.stringify(data));
                     $scope.selfMilk = true;
                     $scope.transactionOk = false;
             });
            }
               $scope.getMilkingTransactions = function(groupId){
               $http.get(BASE_URL + "/group/" + groupId + "/milk")
               .success(function (data, status, headers, config) {
                    $scope.milkingTransactions = data;
                    $scope.fetchedHistory = true;
               }).error(function(data, status, headers, config){
                    $log.info("Error: status = " + status + ", body = " + JSON.stringify(data));
               });
               }
               $scope.$on("successful-transaction", function(event) {
                            $scope.getGroupDetailsAfterMilk($scope.currentGroupId, $scope.currentGroup);
                            $scope.getMilkingTransactions();
               });
                $scope.getGroups();
            });
    })();