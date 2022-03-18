angular.module('info', ['ngResource']).
    factory('AppInfo', function ($resource) {
        return $resource('appinfo');
    });

function InfoController($scope, AppInfo) {
    $scope.info = AppInfo.get();
}
