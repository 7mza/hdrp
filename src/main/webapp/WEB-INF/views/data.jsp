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
            <div class="row">
                <div class="col-md-6">
                    <div class="panel panel-default">
                        <div class="panel-heading">Learning Set : {{ totalEntry }} entries </div>
                        <div class="panel-body">
                            <canvas id="bar" class="chart chart-bar" chart-data="dataEntry" chart-labels="labels"></canvas>
                        </div>
                    </div>
                </div>
                <div class="col-md-6">
                    <div class="panel panel-default">
                        <div class="panel-heading">Testing Set : {{ totalTest }} tests </div>
                        <div class="panel-body">
                            <canvas id="bar" class="chart chart-bar" chart-data="dataTest" chart-labels="labels"></canvas>
                        </div>
                    </div>
                </div>
            </div>
            <div class="row">
                <p>
                    <button ng-click="refresh()" id="refresh" type="button" class="btn btn-primary">Refresh</button>
                    <button ng-click="outliers()" id="reduce" type="button" class="btn btn-warning">Clean outliers</button>
                    <button ng-click="irrelevant()" id="reduce" type="button" class="btn btn-danger">Clean irrelevant</button>
                </p>
            </div>
            <div class="row">
                <div class="col-md-6">
                    <div class="panel panel-default">
                        <div class="panel-heading">Generate Wecka learning Arff</div>
                        <div class="panel-body">
                            <a href="generate/reduce.html" class="btn btn-success" role="button">reduce</a>
                            <a href="generate/sum.html" class="btn btn-success" role="button">reduce-sum</a>
                            <a href="generate/slice.html" class="btn btn-success" role="button">slice</a>
                            <a href="generate/slice/norm.html" class="btn btn-success" role="button">slice-norm</a>
                            <a href="generate/hist.html" class="btn btn-success" role="button">hist</a>
                        </div>
                    </div>
                </div>
                <div class="col-md-6">
                    <div class="panel panel-default">
                        <div class="panel-heading">Generate Matlab learning CSV</div>
                        <div class="panel-body">
                            <a href="generate/reduce/csv.html" class="btn btn-success" role="button">reduce</a>
                            <a href="generate/sum/csv.html" class="btn btn-success" role="button">reduce-sum</a>
                            <a href="generate/slice/csv.html" class="btn btn-success" role="button">slice</a>
                            <a href="generate/slice/norm/csv.html" class="btn btn-success" role="button">slice-norm</a>
                            <a href="generate/hist/csv.html" class="btn btn-success" role="button">hist</a>
                        </div>
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="col-md-6">
                    <div class="panel panel-default">
                        <div class="panel-heading">Generate Wecka test Arff</div>
                        <div class="panel-body">
                            <a href="generate/reduce/test.html" class="btn btn-success" role="button">reduce</a>
                            <a href="generate/sum/test.html" class="btn btn-success" role="button">reduce-sum</a>
                            <a href="generate/slice/test.html" class="btn btn-success" role="button">slice</a>
                            <a href="generate/slice/norm/test.html" class="btn btn-success" role="button">slice-norm</a>
                            <a href="generate/hist/test.html" class="btn btn-success" role="button">hist</a>
                        </div>
                    </div>
                </div>
                <div class="col-md-6">
                    <div class="panel panel-default">
                        <div class="panel-heading">Generate Matlab test CSV</div>
                        <div class="panel-body">
                            <a href="generate/reduce/csv/test.html" class="btn btn-success" role="button">reduce</a>
                            <a href="generate/sum/csv/test.html" class="btn btn-success" role="button">reduce-sum</a>
                            <a href="generate/slice/csv/test.html" class="btn btn-success" role="button">slice</a>
                            <a href="generate/slice/norm/csv/test.html" class="btn btn-success" role="button">slice-norm</a>
                            <a href="generate/hist/csv/test.html" class="btn btn-success" role="button">hist</a>
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
        <script type="text/javascript" src="assets/js/chart.min.js"></script>
        <script type="text/javascript" src="assets/js/angular.min.js"></script>
        <script type="text/javascript" src="assets/js/angular-chart.min.js"></script>
        <script type="text/javascript">
            var app = angular.module('app', ['chart.js']);
            app.controller('ctrl', ['$scope', '$log', '$http', function($scope, $log, $http) {
                $scope.nbr = 0;
                $scope.labels = ['0', '1', '2', '3', '4', '5', '6', '7', '8', '9'];
                $scope.dataEntry = [
                    [0, 0, 0, 0, 0, 0, 0, 0, 0, 0]
                ];
                $scope.dataTest = [
                    [0, 0, 0, 0, 0, 0, 0, 0, 0, 0]
                ];
                $scope.refresh = function() {
                    $http.get('refresh/data.html')
                        .success(function(result) {
                            $scope.dataEntry = [result.distEntry];
                            $scope.totalEntry = result.totalEntry;
                            $scope.dataTest = [result.distTest];
                            $scope.totalTest = result.totalTest;
                        })
                        .error(function(data, status) {});
                };
                $scope.outliers = function() {
                    $http.get('clean/outliers.html')
                        .success(function(result) {
                            $scope.refresh();
                        })
                        .error(function(data, status) {});
                };
                $scope.irrelevant = function() {
                    $http.get('clean/irrelevant.html')
                        .success(function(result) {
                            $scope.refresh();
                        })
                        .error(function(data, status) {});
                };
                $scope.refresh();
            }]);
        </script>
    </body>

    </html>
