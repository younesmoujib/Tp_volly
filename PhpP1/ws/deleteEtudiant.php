<?php

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
if ($_SERVER["REQUEST_METHOD"] == "POST") {
    include_once '../racine.php';
    include_once RACINE . '/service/EtudiantService.php';

    if (isset($_GET['id'])) {
        $id = $_GET['id'];
        deleteEtudiant($id);
    } else {
        header('HTTP/1.1 400 Bad Request');
        echo json_encode(array("message" => "L'ID de l'étudiant est requis pour la suppression."));
    }
}

function deleteEtudiant($id) {
    $es = new EtudiantService();

    $es->delete($es->findById($id));
    header('Content-type: application/json');
    echo json_encode($es->findAllApi());
}
