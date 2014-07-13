<?php
use Phalcon\Mvc\Model,
Phalcon\Mvc\Model\Message,
Phalcon\Mvc\Model\Validator\Uniqueness;

Class User extends \Phalcon\Mvc\Model {

	protected $email;
	protected $password;

	public function getEmail() {
		return $this->email;
	}

	public function getPassword() {
		return $this->password;
	}

	public function validation() {
		$this->validate(new Uniqueness(
			array(
				"field" => "email",
				"message" => "This email address is already registered"
			)
		));

		if ($this->validationHasFailed() == true) {
			return false;
		}
	}

	public static function find($parameters = array()) {
		return parent::find($parameters);
	}

	public static function findFirst($parameters = array()) {
		return parent::findFirst($parameters);
	}

	public function columnMap() {
		return array(
			'email' => 'email',
			'password' => 'password'
		);
	}

	public function getSource() {
		return "User";
	}
}