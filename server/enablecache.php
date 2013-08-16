<?php
	$expires = 60 * 60; // 60 sec * 60 min = 1 hour
	header("Pragma: public");
	header("Cache-Control: maxage=" . $expires);
	header('Expires: ' . gmdate('D, d M Y H:i:s', time() + $expires) . ' GMT');
?>


