<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
    <!DOCTYPE html>
    <html lang="en-us" ng-app="app">

    <head>
        <title>Handwritten Digit Recognition Project</title>
        <meta charset="UTF-8">
        <meta http-equiv="X-UA-Compatible" content="IE=Edge">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <link rel="stylesheet" type="text/css" href="assets/css/bootstrap.min.css" />
        <link rel="stylesheet" type="text/css" href="assets/css/angular-chart.min.css" />
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
            <div class="col-md-6">
                <div class="panel panel-default">
                    <div class="panel-heading">3NN Performance</div>
                    <div class="panel-body">
                        <canvas id="bar" class="chart chart-bar" chart-data="data3nn" chart-labels="labels" chart-series="series"></canvas>
                    </div>
                </div>
            </div>
            <div class="col-md-6">
                <div class="panel panel-default">
                    <div class="panel-heading">5NN Performance</div>
                    <div class="panel-body">
                        <canvas id="bar" class="chart chart-bar" chart-data="data5nn" chart-labels="labels" chart-series="series"></canvas>
                    </div>
                </div>
            </div>
            <div class="col-md-6">
                <div class="panel panel-default">
                    <div class="panel-heading">7NN Performance</div>
                    <div class="panel-body">
                        <canvas id="bar" class="chart chart-bar" chart-data="data7nn" chart-labels="labels" chart-series="series"></canvas>
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
        <script type="text/javascript" src="assets/js/chart.min.js"></script>
        <script type="text/javascript" src="assets/js/angular.min.js"></script>
        <script type="text/javascript" src="assets/js/angular-chart.min.js"></script>
        <script type="text/javascript">
            var app = angular.module('app', ['chart.js']);
            app.controller('ctrl', ['$scope', '$log', '$http', function($scope, $log, $http) {
                $scope.series = ['Successful', 'Failed'];
                $scope.labels = ['0', '1', '2', '3', '4', '5', '6', '7', '8', '9'];
                $scope.data3nn = [
                    [0, 0, 0, 0, 0, 0, 0, 0, 0, 0],
                    [0, 0, 0, 0, 0, 0, 0, 0, 0, 0]
                ];
                $scope.data5nn = [
                    [0, 0, 0, 0, 0, 0, 0, 0, 0, 0],
                    [0, 0, 0, 0, 0, 0, 0, 0, 0, 0]
                ];
                $scope.data7nn = [
                    [0, 0, 0, 0, 0, 0, 0, 0, 0, 0],
                    [0, 0, 0, 0, 0, 0, 0, 0, 0, 0]
                ];
                $scope.refresh = function() {
                    $http.get('refresh/stats.html')
                        .success(function(result) {
                            $scope.data3nn = result.data3nn;
                            $scope.data5nn = result.data5nn;
                            $scope.data7nn = result.data7nn;
                        })
                        .error(function(data, status) {});
                };
                $scope.refresh();
            }]);
        </script>
    </body>

    </html>
