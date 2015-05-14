(function () {
    var BASE_URL = "http://localhost:7070";
    var milked = angular.module('milked', []);

     milked.controller("GroupController", function ($http, $scope, $log, $rootScope) {

                $scope.groupDetails = [];
                $scope.groupRequest = {};
                $scope.groups = [];

                $scope.loadGroup = function(group){
                    alert(group.name);
                }

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

                $scope.getGroupDetails = function() {
                                $http.get(BASE_URL + "/group/1/users", $scope.GroupDetails).
                                    success(function (data, status, headers, config) {
                                    if(data == null){
                                        console.log("Data is null");
                                    }
                                        $scope.groupDetails = data;
                                        console.log(data);
                                    }).error(function(data, status, headers, config) {
                                        $log.info("Error: status = " + status + ", body = " + JSON.stringify(data));
                                    });
                            }

                $scope.getGroups();
                $scope.getGroupDetails();

            });
    })();