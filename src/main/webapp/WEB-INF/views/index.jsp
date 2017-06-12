<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
    <!DOCTYPE html>
    <html lang="en-us" ng-app="app">

    <head>
        <title>Handwritten Digit Recognition Project</title>
        <meta charset="UTF-8">
        <meta http-equiv="X-UA-Compatible" content="IE=Edge">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <link rel="stylesheet" type="text/css" href="assets/css/bootstrap.min.css" />
        <link rel="stylesheet" type="text/css" href="assets/css/mine.min.css" />
    </head>

    <body>
        <header>
            <nav class="navbar navbar-default">
                <div class="container">
                    <ul class="nav navbar-nav navbar-left">
                        <li>
                            <a href="index.html">
                                <i class="fa fa-home">Learning</i>
                            </a>
                        </li>
                        <li>
                            <a href="data.html">
                                <i class="fa fa-home">Dataset</i>
                            </a>
                        </li>
                        <li>
                            <a href="test.html">
                                <i class="fa fa-home">Testing</i>
                            </a>
                        </li>
                        <li>
                            <a href="stats.html">
                                <i class="fa fa-home">Statistics</i>
                            </a>
                        </li>
                    </ul>
                </div>
            </nav>
        </header>
        <div ng-controller="ctrl" class="container">
            <div class="row">
                <div class="col-md-3">
                    <canvas ng-model="canvas" ng-click="enable()" class="panel panel-default" width="200" height="200"></canvas>
                    <div class="input-group">
                        <span class="input-group-addon" id="basic-addon1">Digit</span>
                        <input ng-model="digit" type="number" id="digit" class="form-control" type="text">
                    </div>
                </div>
                <div class="col-md-3">
                    <p>
                        <label class="radio-inline">
                            <input ng-model="test" ng-value="0" type="radio" name="test">Entry
                        </label>
                        <label class="radio-inline">
                            <input ng-model="test" ng-value="1" type="radio" name="test">Test
                        </label>
                    </p>
                    <p>
                        <button ng-click="clear()" id="clear" type="button" class="btn btn-default">Clear</button>
                    </p>
                    <p>
                        <button ng-click="add()" ng-disabled="modif == false" type="button" class="btn btn-success">Add</button>
                    </p>
                </div>
            </div>
        </div>
        <footer class="footer">
            <div class="container">
                <p class="text-muted">OCRP - Baaziz, Amyar, Bendari - M1 WI UJM 2015-16</p>
            </div>
        </footer>
        <script type="text/javascript" src="assets/js/jquery-2.2.1.min.js"></script>
        <script type="text/javascript" src="assets/js/sketch.min.js"></script>
        <script type="text/javascript" src="assets/js/angular.min.js"></script>
        <script type="text/javascript">
            var app = angular.module('app', []);
            app.controller('ctrl', ['$scope', '$log', '$http', function($scope, $log, $http) {
                // init
                $scope.test = 0;
                $scope.modif = false;
                $scope.canvas = angular.element('canvas');
                $scope.context = $scope.canvas[0].getContext('2d');
                $scope.canvas.sketch({
                    defaultColor: "#000000",
                    defaultSize: 10
                });
                // enable save button if user draw on canvas
                $scope.enable = function() {
                    $scope.modif = true;
                };
                // convert from rgba to an array with 0 for white/transparent 1 for anything else
                $scope.toArray = function() {
                    $scope.canvas = angular.element('canvas');
                    $scope.context = $scope.canvas[0].getContext('2d');
                    var imageData = $scope.context.getImageData(0, 0, $scope.canvas[0].width, $scope.canvas[0].height);
                    var data = imageData.data;
                    var array = [];
                    for (var i = 0; i < data.length; i += 4) {
                        var sum = data[i] + data[i + 1] + data[i + 2] + data[i + 3];
                        if (sum > 0)
                            array.push(1);
                        else
                            array.push(0);
                    }
                    return array;
                };
                // send user's input array to server
                $scope.add = function() {
                    var form = new FormData();
                    form.append('pixels', $scope.toArray());
                    form.append('digit', $scope.digit);
                    form.append('test', $scope.test);
                    $http.post('add.html', form, {
                            withCredentials: true,
                            headers: {
                                'Content-Type': undefined
                            },
                            transformRequest: angular.identity
                        })
                        .success(function(result) {})
                        .error(function(data, status) {});
                    // clear canvas
                    img = null;
                    $scope.clear();
                    // disable save button
                    $scope.modif = false;
                };
                // clear canvas
                $scope.clear = function() {
                    $scope.context.clearRect(0, 0, $scope.canvas[0].width, $scope.canvas[0].height);
                    $scope.canvas.sketch('actions', []);
                    $scope.modif = false;
                }
            }]);
        </script>
    </body>

    </html>
