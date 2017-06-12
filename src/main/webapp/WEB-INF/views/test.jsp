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
                <div class="col-md-4">
                    <canvas ng-model="canvas" ng-click="enable()" class="panel panel-default" width="200" height="200"></canvas>
                    <div class="input-group">
                        <span class="input-group-addon" id="basic-addon1">Class</span>
                        <input ng-model="digit" ng-readonly="true" id="digit" class="form-control" type="text">
                    </div>
                </div>
                <div class="col-md-4">
                    <p>
                        <label class="radio-inline">
                            <input ng-model="algo" ng-value="3" type="radio" name="algo">3 NN
                        </label>
                        <label class="radio-inline">
                            <input ng-model="algo" ng-value="5" type="radio" name="algo">5 NN
                        </label>
                        <label class="radio-inline">
                            <input ng-model="algo" ng-value="7" type="radio" name="algo">7 NN
                        </label>
                    </p>
                    <p>
                        <button ng-click="clear()" id="clear" type="button" class="btn btn-secondary">Clear</button>
                    </p>
                    <p>
                        <button ng-click="classify()" ng-disabled="modif == false" id="send" type="button" class="btn btn-primary">Classify</button>
                    </p>
                </div>
                <div class="col-md-4">
                    <div class="panel panel-default">
                        <div class="panel-heading">Testing batches </div>
                        <div class="panel-body">
                            <p>
                                <button ng-click="batch()" id="batch" type="button" class="btn btn-success btn-lg">Batch</button>
                            </p>
                        </div>
                    </div>
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
                $scope.algo = 3;
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
                $scope.classify = function() {
                    var form = new FormData();
                    form.append('pixels', $scope.toArray());
                    form.append('algo', $scope.algo);
                    $http.post('classify.html', form, {
                            withCredentials: true,
                            headers: {
                                'Content-Type': undefined
                            },
                            transformRequest: angular.identity
                        })
                        .success(function(result) {
                            $scope.digit = result;
                        })
                        .error(function(data, status) {});
                    // clear canvas
                    // img = null;
                    // $scope.clear();
                    // disable save button
                    // $scope.modif = false;
                };
                // clear canvas
                $scope.clear = function() {
                    $scope.context.clearRect(0, 0, $scope.canvas[0].width, $scope.canvas[0].height);
                    $scope.canvas.sketch('actions', []);
                    $scope.modif = false;
                    $scope.digit = '';
                };
                $scope.batch = function() {
                    $http.get('test/batch.html')
                        .success(function(result) {})
                        .error(function(data, status) {});
                };
            }]);
        </script>
    </body>

    </html>
