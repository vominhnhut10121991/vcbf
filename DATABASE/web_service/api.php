<?php
	require_once("Rest.inc.php");
	
	class API extends REST {
		public $db = null;
		
		private $update_next = true;
		private $month;
		
		public function __construct(){			
			parent::__construct();				// Init parent contructor
			date_default_timezone_set('Asia/Ho_Chi_Minh');
			$this->connectToDatabase();
			$this->db->query('SET CHARACTER SET utf8');

		}
		
		public function connectToDatabase() { 			
			$this->db = new pdo('mysql:host=localhost;dbname=vcbf', 'root', '');
			
		}
	
		/*
		 * Public method for access api.
		 * This method dynmically call the method based on the query string
		 *
		 */
		public function processApi(){
			if (isset($_REQUEST['request'])) {
				$func = strtolower(trim(str_replace("/", "", $_REQUEST['request'])));
				if((int) method_exists($this, $func) > 0) {
					$this->$func();
				}
				else {
					$this->response('', 404);				// If the method not exist with in this class, response would be "Page not found".
				}	
			}
			else {
				$this->response('', 403);
			}
		}
		
		private function authenticate() {
			$stmt = $this->db->query('SELECT * FROM vcbf_user');
			$response = 'false';
			while($row = $stmt->fetch(PDO::FETCH_ASSOC)) {
				if ($_REQUEST['username'] == $row['username'] && $_REQUEST['password'] == $row['password']) {
					$response = 'true';
					break;
				}	
			}
			if ($response == 'true') {			
				$stmt = $this->db->query('SELECT * FROM vcbf_user WHERE username = "' . $row['username'] . '"');
				$this->response(json_encode($stmt->fetchAll(PDO::FETCH_ASSOC)), 200);
			} else {
				$this->response(json_encode($response), 200);
			}
		}
		private function getNavBCF()  {
			$stmt = $this->db->query('SELECT * FROM vcbf_nav_bcf');
			$this->response(json_encode($stmt->fetchAll(PDO::FETCH_ASSOC)), 200);
		} 
		
		private function getNavTBF()  {
			$stmt = $this->db->query('SELECT * FROM vcbf_nav_tbf');
			$this->response(json_encode($stmt->fetchAll(PDO::FETCH_ASSOC)), 200);
		} 		
		
		private function getNews()  {
			$stmt = $this->db->query('SELECT * FROM vcbf_news');
			$this->response(json_encode($stmt->fetchAll(PDO::FETCH_ASSOC)), 200);
		} 
		
		private function getPortfolioDetailBCF() {
			$stmt = $this->db->query('SELECT * FROM vcbf_portfolio_detail_bcf');
			$this->response(json_encode($stmt->fetchAll(PDO::FETCH_ASSOC)), 200);
		}
		
		private function getPortfolioDetailTBF() {
			$stmt = $this->db->query('SELECT * FROM vcbf_portfolio_detail_tbf');
			$this->response(json_encode($stmt->fetchAll(PDO::FETCH_ASSOC)), 200);
		}
		
		private function getPortfolioStatisticBCF() {
			$stmt = $this->db->query('SELECT * FROM vcbf_portfolio_statistic_bcf');
			$this->response(json_encode($stmt->fetchAll(PDO::FETCH_ASSOC)), 200);
		}
		
		private function getPortfolioStatisticTBF() {
			$stmt = $this->db->query('SELECT * FROM vcbf_portfolio_statistic_tbf');
			$this->response(json_encode($stmt->fetchAll(PDO::FETCH_ASSOC)), 200);
		}
		
		private function getPortfolioTopHoldingBCF() {
			$stmt = $this->db->query('SELECT * FROM vcbf_portfolio_top_holding_bcf');
			$this->response(json_encode($stmt->fetchAll(PDO::FETCH_ASSOC)), 200);
		}
		
		private function getPortfolioTopHoldingTBF() {
			$stmt = $this->db->query('SELECT * FROM vcbf_portfolio_top_holding_tbf');
			$this->response(json_encode($stmt->fetchAll(PDO::FETCH_ASSOC)), 200);
		}
		
		private function getInformationMessage() {
			$stmt = $this->db->query("SELECT * FROM vcbf_information_message where date > '" . $_REQUEST['date'] . "'");
			$this->response(json_encode($stmt->fetchAll(PDO::FETCH_ASSOC)), 200);
		}
		
		private function getRemindingMessage() {
			$row;
			$id;
			$reminding_date;
			foreach($this->db->query('SELECT * FROM vcbf_reminding_message') as $r) {
				$id = $r['id'];
				$reminding_date = $r['date'];
				$row = $r;
			}			
			
			$year = substr($reminding_date,0,4);
			$month = substr($reminding_date,5,7);
			$day = substr($reminding_date,8,10);
			$hour = substr($reminding_date,11,13);
			$minute = substr($reminding_date,14,16);
			$second = substr($reminding_date,17,19);
			
			$current_time = getDate();
			$formatted_current_time = $current_time['year'] . "-" . $current_time['mon'] . "-" . $current_time['mday'] . " " . $current_time['hours'] . ":" . $current_time['minutes'] . ":" . $current_time['seconds'];			
			if (strcmp(date_diff(new DateTime($reminding_date),new DateTime($formatted_current_time))->format('%R'),"+") == 0) {
				$this->update_next = false;
			}
			
			if(!$this->update_next) {
				$this->update_next = true;
				if($day > 28) {
					if($month == 1) {
						if($year % 4 == 0) {
							$day = 29;
						}
						else {
							$day = 28;
						}
					} else if($day == 31) {
						if($month != 7 && $month != 12) {
							$day = 30;
						}
					}
				}
				
				if($month < 12) {
					$month = $month + 1;
				} else {
					$month = 1;
					$year = $year + 1;
				}
				$this->db->exec("UPDATE vcbf_reminding_message SET date='".$year."-".$month."-".$day." ".substr($reminding_date,11,strlen($reminding_date))."' WHERE id=".$id.";");
			}
			$stmt = $this->db->query('SELECT * FROM vcbf_reminding_message');
			$this->response(json_encode($stmt->fetchAll(PDO::FETCH_ASSOC)), 200);
		}		
		
		private function getWeeklyFundPriceBCF() {
			$stmt = $this->db->query('SELECT * FROM vcbf_weekly_fund_price_bcf');
			$this->response(json_encode($stmt->fetchAll(PDO::FETCH_ASSOC)), 200);
		}
		
		private function getWeeklyFundPriceTBF() {
			$stmt = $this->db->query('SELECT * FROM vcbf_weekly_fund_price_tbf');
			$this->response(json_encode($stmt->fetchAll(PDO::FETCH_ASSOC)), 200);
		}
 		
		private function getPortfolioIndustryBreakdownBCF() {
			$stmt = $this->db->query('SELECT * FROM vcbf_portfolio_industry_breakdown_bcf');
			$this->response(json_encode($stmt->fetchAll(PDO::FETCH_ASSOC)), 200);
		}
		
		private function getPortfolioIndustryBreakdownTBF() {
			$stmt = $this->db->query('SELECT * FROM vcbf_portfolio_industry_breakdown_tbf');
			$this->response(json_encode($stmt->fetchAll(PDO::FETCH_ASSOC)), 200);
		}
				
			
		private function getVersion() {
			$stmt = $this->db->query('SELECT * FROM vcbf_version');
			$this->response(json_encode($stmt->fetchAll(PDO::FETCH_ASSOC)), 200);
		}
		
		private function getTime() {
			$current_time = getDate();
			$this->response($current_time['year'] . "-" . $current_time['mon'] . "-" . $current_time['mday'] . " " . $current_time['hours'] . ":" . $current_time['minutes'] . ":" . $current_time['seconds'], 200);		
		}
		
	}
	
	// Initiate Library	
	$api = new API;
	$api->processApi();
?>