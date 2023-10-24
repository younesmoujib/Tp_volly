<?php
if ($_SERVER["REQUEST_METHOD"] == "POST") {
    include_once '../racine.php';
    include_once RACINE . '/service/EtudiantService.php';
    update();
}

function update() {
    
if (isset($_GET['id'])) {
    
    $id = $_GET['id'];
    $nom = $_GET['nom'];
    $prenom = $_GET['prenom'];
    $ville = $_GET['ville'];
    $sexe = $_GET['sexe'];

    $es = new EtudiantService();
    
    $upetudiant = $es->findById($id);
    $upetudiant->setNom($nom);
    $upetudiant->setPrenom($prenom);
    $upetudiant->setVille($ville);
    $upetudiant->setSexe($sexe);
    
   $es->update($upetudiant);
    

}}