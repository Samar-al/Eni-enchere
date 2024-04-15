$(document).ready(function() {
    $("#filtersForm").on("submit", function(event) {
        event.preventDefault(); // Empêche le formulaire d'être soumis normalement

        // Récupère les valeurs des champs de formulaire
        var filters = $("#filtres").val();
        var category = $("#categories").val();

        // Envoie une requête HTTP GET à votre contrôleur Spring
        $.get("/encheres/search", { filters: filters, category: category }, function(data) {
            // Met à jour la liste d'enchères avec les résultats de la recherche
            $(".listBids").html(data);
        });
    });
});