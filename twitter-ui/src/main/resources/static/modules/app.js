angular.module('TwitterVisualizer', ['ngRoute', 'info', 'report']).
config(['$routeProvider',
         function($routeProvider) {
           $routeProvider.
             when('/errors', {
               templateUrl: '/templates/errors.html'
             }).
             otherwise({
            	 templateUrl: '/templates/main.html'
             });
         }]);
