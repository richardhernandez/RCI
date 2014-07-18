<?php
	require('password.php');
	$loader = new \Phalcon\Loader();

	$loader->registerDirs(array(
		__DIR__ . '/models/'
	))->register();

	$di = new \Phalcon\DI\FactoryDefault();

	$di->set('db', function() {
		return new \Phalcon\Db\Adapter\Pdo\Mysql(array(
			"host" => "localhost",
			"username" => "root",
			"password" => "RciDBPassword",
			"dbname" => "rci"
		));
	});

	$app = new \Phalcon\Mvc\Micro($di);
	
	// POSTs
	// Create a new user
	$app->post('/api/user', function() use ($app) {
		$user = $app->request->getJsonRawBody();
		$phql = "INSERT INTO User (email, password) VALUES (:email:, :password:)";
		$status = $app->modelsManager->executeQuery($phql, array(
			'email' => $user->email,
			'password' => password_hash($user->password, PASSWORD_BCRYPT)
		));

		$response = new Phalcon\Http\Response();
		if ($status->success() == true) {
			$response->setStatusCode(201, "Created");
			$response->setJsonContent(array('status' => 'OK', 'data' => $user));
		}
		else {
			$response->setStatusCode(409, "Conflict");
			$errors = array();
			foreach ($status->getMessages() as $message) {
				$errors[] = $message->getMessage();
			}

			$response->setJsonContent(array('status' => 'ERROR', 'messages' => $errors));
		}

		return $response;
	});

	// Authorizes a user's login attempt
	$app->post('/api/login', function() use ($app) {
		$user = $app->request->getJsonRawBody();
		$phql = "SELECT * FROM User WHERE email=:email:";
		$status = $app->modelsManager->executeQuery($phql, array(
			'email' => $user->email
		))->getFirst();


		$response = new Phalcon\Http\Response();
		if (password_verify($user->password, $status->getPassword()) == true) {
			$response->setStatusCode(200, "OK");
			$response->setJsonContent(array(
				'status' => 'OK',
				'email' => $user->email
			));
		}
		else {
			$response->setStatusCode(401, "Unauthorized");
			$response->setJsonContent(array(
				'status' => 'ERROR',
				'messages' => 'Incorrect password or username'
			));
		}

		return $response;
	});

	// Adds a new SSID to the database and affiliates it with a user's email address
	$app->post('/api/ssid', function() use ($app) {
		$ssid = $app->request->getJsonRawBody();
		$phql = "INSERT INTO SSID (ssid, bssid, adminPassword, gLat, gLong, email) VALUES (:ssid:, :bssid:, :adminPassword:, :gLat:, :gLong:, :email:)";
		$status = $app->modelsManager->executeQuery($phql, array(
			'ssid' => $ssid->ssid,
			'bssid' => $ssid->bssid,
			'adminPassword' => $ssid->adminPassword,
			'gLat' => $ssid->gLat,
			'gLong' => $ssid->gLong,
			'email' => $ssid->email
		));

		$response = new \Phalcon\Http\Response();
		if ($status->success()) {
			$response->setStatusCode(201, "Created");
			$response->setJsonContent(array('status' => 'OK', 'data' => $ssid));
		}
		else {
			$response->setStatusCode(409, "Conflict");
			$errors = array();
			foreach ($status->getMessages() as $message) {
				$errors[] = $message->getMessage();
			}

			$response->setJsonContent(array('status' => 'ERROR', 'messages' => $errors));
		}

		return $response;

	});

	// GETs
	// Dump User table (PLEASE REMOVE WHEN DONE TESTING)
	$app->get('/api/user', function() use ($app) {
		$phql = "SELECT * FROM User";
		$users = $app->modelsManager->executeQuery($phql);

		$data = array();
		if (!empty($users)) {
			foreach ($users as $User) {
				$data[] = array(
					'email' => $User->getEmail(),
					'password' => $User->getPassword()
				);
			}
		}

		echo json_encode($data);
	});

	// Returns a user's password
	// TESTING ONLY PLEASE DELETE
	$app->get('/api/user/{email}', function($email) use ($app) {
		$phql = "SELECT * FROM User WHERE email=:email:";
		$user = $app->modelsManager->executeQuery($phql, array(
			'email' => $email
		))->getFirst();

		$response = new \Phalcon\Http\Response();

		if ($user == false) {
			$response->setStatusCode(404, "Not Found");
			$response->setJsonContent(array('status' => 'NOT-FOUND'));
    	}
    	else {
    		$response->setStatusCode(200, "OK");
    		$response->setJsonContent(array(
    			'status' => 'FOUND',
    			'password' => $user->getPassword()
    		));
   		}

		return $response;
	});

	// Dump SSID table (PLEASE REMOVE WHEN DONE TESTING)
	$app->get('/api/ssid', function() use ($app) {
		$phql = "SELECT * FROM SSID";
		$ssids = $app->modelsManager->executeQuery($phql);

		$data = array();
		if (!empty($ssids)) {
			foreach ($ssids as $ssid) {
				$data[] = array(
					'ssid' => $ssid->getSsid(),
					'bssid' => $ssid->getBssid(),
					'adminPassword' => $ssid->getAdminPassword(),
					'gLat' => $ssid->getGlat(),
					'gLong' => $ssid->getGlong(),
					'email' => $ssid->getEmail()
				);
			}
		}

		echo json_encode($data);
	});

	$app->notFound(function() use ($app) {
		$app->response->setStatusCode(404, 'Not Found')->sendHeaders();
		echo 'something went wrong: 404';
	});

	$app->handle();
?>