$(document).ready(function() {
    $("#filtersForm").on("submit", function(event) {
        event.preventDefault(); // Empêche le formulaire d'être soumis normalement

        // Récupère les valeurs des champs de formulaire
        let filters = $("#filtres").val();
        let category = $("#categories").val();
        let currentSale = null;
        let salesNotStarted = null;

        if (document.getElementById("ventesEnCours").checked) {
            currentSale = $("#ventesEnCours").val();
        }

        if (document.getElementById("ventesNonDebutees").checked) {
            salesNotStarted = $("#ventesNonDebutees").val();
        }

        // Envoie une requête HTTP GET à votre contrôleur Spring
        $.get("/encheres/search", { filters: filters, category: category, currentSale: currentSale, salesNotStarted: salesNotStarted }, function(data) {
            // Met à jour la liste d'enchères avec les résultats de la recherche
            $(".listBidsContainer").html(data);
        });
    });
});