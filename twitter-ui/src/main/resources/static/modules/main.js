angular.module('main', [ '' ]);

function MainController($scope) {

	$scope.setMainView = function(viewName) {
		$scope.viewName = viewName;
		$scope.mainView = "/templates/" + viewName + ".html";
	};

	$scope.init = function() {
		$scope.viewName = "dashboard";
		$scope.setMainView("dashboard");
	};
}