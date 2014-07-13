<?php
use Phalcon\Mvc\Model,
Phalcon\Mvc\Model\Validator\Uniqueness;

Class SSID extends \Phalcon\Mvc\Model {

	protected $ssid;
	protected $bssid;
	protected $adminPassword;
	protected $gLat;
	protected $gLong;
	protected $email;

	public function getSsid() {
		return $this->ssid;
	}

	public function getBssid() {
		return $this->password;
	}

	public function getAdminPassword() {
		return $this->adminPassword;
	}

	public function getGlat() {
		return $this->gLat;
	}

	public function getGlong() {
		return $this->gLong;
	}

	public function getEmail() {
		return $this->email;
	}

	public function validation() {
		$this->validate(new Uniqueness(
			array(
				"field" => "email",
				"field" => "ssid"
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
			'ssid' => 'ssid',
			'bssid' => 'bssid',
			'adminPassword' => 'adminPassword',
			'gLat' => 'gLat',
			'gLong' => 'gLong',
			'email' => 'email'
		);
	}

	public function getSource() {
		return "SSID";
	}
}