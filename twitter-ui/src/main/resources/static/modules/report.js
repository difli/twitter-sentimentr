	angular.module('report', ['ngResource', 'ui.bootstrap']).
	    factory('TweetData', function ($resource) {
	        return $resource('tweet_data/languageandsentiment');
	    }).
	    factory('TweetDataLanguages', function ($resource) {
	        return $resource('tweet_data/search/languages');
	    }).
	    controller('ReportController', function ($scope, TweetData, TweetDataLanguages) {
	        function list() {
	            TweetData.query({lang: $scope.language, sentiment: $scope.sentiment}).$promise.then(function(tweet_data) {
	                $scope.tweet_data = tweet_data;
	            })
	        }

	        function queryForLanguages() {
	            TweetDataLanguages.query().$promise.then(function(languageOptions) {
		    		for(var i=0;i<languageOptions.length;i++){
	                $scope.languageOptions[i] = languageOptions[i].lang;
	                }
	        	})
	        }

	        $scope.refresh = function() {
	                list();
	        };

	        $scope.queryForLanguages = function() {
	            queryForLanguages();
	        };

	        $scope.search = function() {
	            search();
	        };

	        $scope.init = function() {
	            $scope.sentiment = 2;
	            $scope.sentimentOptions = [0,1,2,3,4];
	            $scope.language = 'en';
	            $scope.languageOptions = ['en'];
	            $scope.tweet_data;
				$scope.queryForLanguages();
	            $scope.refresh();
	        };
	    });
