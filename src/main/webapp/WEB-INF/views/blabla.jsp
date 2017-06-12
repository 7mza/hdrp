<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<!DOCTYPE html>
<html lang="en-us" ng-app="app">

<head>
<title>Handwritten Digit Recognition Project</title>
<meta charset="UTF-8">
<meta http-equiv="X-UA-Compatible" content="IE=Edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet" type="text/css"
	href="assets/css/bootstrap.min.css" />
<link rel="stylesheet" type="text/css"
	href="assets/css/angular-chart.min.css" />
<link rel="stylesheet" type="text/css" href="assets/css/mine.min.css" />
</head>

<body>
	<header>
		<nav class="navbar navbar-default">
			<div class="container">
				<ul class="nav navbar-nav navbar-left">
					<li><a href="#"> <i class="fa fa-home">#</i>
					</a></li>
				</ul>
			</div>
		</nav>
	</header>
	<div ng-controller="ctrl" class="container">
		<div class="row">
			<div class="col-md-6">
				<div class="panel panel-default">
					<div class="panel-heading">NeuralNet Slice
						entries</div>
					<div class="panel-body">
						<canvas id="bar" class="chart chart-bar" chart-data="data"
							chart-labels="labels"></canvas>
					</div>
				</div>
			</div>
			<div class="col-md-6">
				<div class="panel panel-default">
					<div class="panel-heading">NeuralNet Slice
						entries</div>
					<div class="panel-body">
						<canvas id="pie" class="chart chart-pie" chart-data="diti"
							chart-labels="libels" chart-legend="true">
						</canvas>
					</div>
				</div>
			</div>
		</div>
	</div>
	<script type="text/javascript" src="assets/js/jquery-2.2.1.min.js"></script>
	<script type="text/javascript" src="assets/js/chart.min.js"></script>
	<script type="text/javascript" src="assets/js/angular.min.js"></script>
	<script type="text/javascript" src="assets/js/angular-chart.min.js"></script>
	<script type="text/javascript">
		var app = angular.module('app', [ 'chart.js' ]);
		app.controller('ctrl', [
				'$scope',
				'$log',
				'$http',
				function($scope, $log, $http) {
					$scope.nbr = 0;
					$scope.labels = [ '0', '1', '2', '3', '4', '5', '6', '7',
							'8', '9' ];
					$scope.data = [ [ 29, 18, 25, 24, 27, 27, 26, 29, 24, 20 ],
							[ 1, 12, 5, 6, 3, 3, 4, 1, 6, 10 ] ];

					$scope.libels = [ "Success", "Fail" ];
					$scope.diti = [ 249, 51 ];
				} ]);
	</script>
</body>

</html>
