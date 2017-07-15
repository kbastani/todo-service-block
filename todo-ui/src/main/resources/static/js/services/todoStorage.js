/*global angular */

/**
 * Services that persists and retrieves todos from localStorage or a backend API
 * if available.
 *
 * They both follow the same API, returning promises for all changes to the
 * model.
 */
angular.module('todomvc')
    .factory('todoStorage', function ($http, $injector) {
        'use strict';

        // Detect if an API backend is present. If so, return the API module, else
        // hand off the localStorage adapter
        return $http.get('/api')
            .then(function () {
                return $injector.get('api');
            }, function () {
                return $injector.get('localStorage');
            });
    })
    .factory('api', function ($resource, traverson) {
        'use strict';
        var store = {
            todos: [],
            resource: $resource('/api/v1/todos/:todoId', null,
                {
                    update: {method: 'PUT'}
                }
            ),
            traverson: function () {
                return traverson.from('/api/v1/todos/{todoId}');
            },
            clearCompleted: function () {
                var completeTodos = store.todos.filter(function (todo) {
                    return todo.status == "COMPLETED";
                });

                completeTodos.forEach(store.delete);
            },

            delete: function (todo) {
                var originalTodos = store.todos.slice(0);

                store.todos.splice(store.todos.indexOf(todo), 1);
                return store.resource.delete({todoId: todo.todoId},
                    function () {
                    }, function error() {
                        angular.copy(originalTodos, store.todos);
                    });
            },

            get: function () {
                return store.resource.query(function (resp) {
                    resp.forEach(function (item) {
                        item.completed = item.status == "COMPLETED";
                    });
                    angular.copy(resp, store.todos);
                });
            },

            insert: function (todo) {
                var originalTodos = store.todos.slice(0);

                return store.resource.save(todo,
                    function success(resp) {
                        todo.todoId = resp.todoId;
                        store.todos.push(todo);
                    }, function error() {
                        angular.copy(originalTodos, store.todos);
                    })
                    .$promise;
            },

            command: function (todo) {
                function getCommand(status) {
                    var result;

                    switch (status) {
                        case "ACTIVE":
                            result = "activate";
                            break;
                        case "COMPLETED":
                            result = "complete";
                            break;
                    }

                    return result;
                }

                var command = getCommand(todo.status);

                return store.traverson()
                    .withTemplateParameters({todoId: todo.todoId})
                    .json()
                    .follow("$._links.commands.href", "$._links." + command + ".href")
                    .getResource()
                    .result;
            },

            put: function (todo) {
                return store.resource.update({todoId: todo.todoId}, todo)
                    .$promise;
            }
        };

        return store;
    })

    .factory('localStorage', function ($q) {
        'use strict';

        var STORAGE_ID = 'todos-angularjs';

        var store = {
            todos: [],

            _getFromLocalStorage: function () {
                return JSON.parse(localStorage.getItem(STORAGE_ID) || '[]');
            },

            _saveToLocalStorage: function (todos) {
                localStorage.setItem(STORAGE_ID, JSON.stringify(todos));
            },

            clearCompleted: function () {
                var deferred = $q.defer();

                var incompleteTodos = store.todos.filter(function (todo) {
                    return todo.status == "ACTIVE";
                });

                angular.copy(incompleteTodos, store.todos);

                store._saveToLocalStorage(store.todos);
                deferred.resolve(store.todos);

                return deferred.promise;
            },

            delete: function (todo) {
                var deferred = $q.defer();

                store.todos.splice(store.todos.indexOf(todo), 1);

                store._saveToLocalStorage(store.todos);
                deferred.resolve(store.todos);

                return deferred.promise;
            },

            get: function () {
                var deferred = $q.defer();

                angular.copy(store._getFromLocalStorage(), store.todos);
                deferred.resolve(store.todos);

                return deferred.promise;
            },

            insert: function (todo) {
                var deferred = $q.defer();

                store.todos.push(todo);

                store._saveToLocalStorage(store.todos);
                deferred.resolve(store.todos);

                return deferred.promise;
            },

            put: function (todo, index) {
                var deferred = $q.defer();

                store.todos[index] = todo;

                store._saveToLocalStorage(store.todos);
                deferred.resolve(store.todos);

                return deferred.promise;
            }
        };

        return store;
    });