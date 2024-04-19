function achatsRadio(event) {
    if (event.checked == true) {
        document.getElementById("ventesEnCours").disabled = true;
        document.getElementById("ventesNonDebutees").disabled = true;
        document.getElementById("ventesTerminees").disabled = true;

        document.getElementById("ventesEnCours").checked = false;
        document.getElementById("ventesNonDebutees").checked = false;
        document.getElementById("ventesTerminees").checked = false;

        document.getElementById("encheresOuvertes").disabled = false;
        document.getElementById("enchetesEnCours").disabled = false;
        document.getElementById("encheresRemportees").disabled = false;
    }
}

function ventesRadio(event) {
    if (event.checked == true) {
        document.getElementById("ventesEnCours").disabled = false;
        document.getElementById("ventesNonDebutees").disabled = false;
        document.getElementById("ventesTerminees").disabled = false;

        document.getElementById("encheresOuvertes").disabled = true;
        document.getElementById("enchetesEnCours").disabled = true;
        document.getElementById("encheresRemportees").disabled = true;

        document.getElementById("encheresOuvertes").checked = false;
        document.getElementById("enchetesEnCours").checked = false;
        document.getElementById("encheresRemportees").checked = false;
    }
}

$(document).ready(function() {
    $("#filtersForm").on("submit", function(event) {
        event.preventDefault(); // Empêche le formulaire d'être soumis normalement

        // Récupère les valeurs des champs de formulaire
        let filters = $("#filtres").val();
        let category = $("#categories").val();

        let openBids = null;
        let myCurrentBids = null;
        let wonBids = null;
        let currentSale = null;
        let salesNotStarted = null;
        let completedSales = null;

        if (document.getElementById("encheresOuvertes") != null && document.getElementById("encheresOuvertes").checked) {
            openBids = $("#encheresOuvertes").val();
        }

        if (document.getElementById("enchetesEnCours") != null && document.getElementById("enchetesEnCours").checked) {
            //Mis à 1, pour éviter un conflit avec les encheres ouvertes au moment de la récupération de la valeur
            myCurrentBids = 1;
        }

        if (document.getElementById("encheresRemportees") != null && document.getElementById("encheresRemportees").checked) {
            wonBids = $("#encheresRemportees").val();
        }

        if (document.getElementById("ventesEnCours") != null && document.getElementById("ventesEnCours").checked) {
            currentSale = $("#ventesEnCours").val();
        }

        if (document.getElementById("ventesNonDebutees") != null && document.getElementById("ventesNonDebutees").checked) {
            salesNotStarted = $("#ventesNonDebutees").val();
        }

        if (document.getElementById("ventesTerminees") != null && document.getElementById("ventesTerminees").checked) {
            completedSales = $("#ventesTerminees").val();
        }

        // Envoie une requête HTTP GET à votre contrôleur Spring
        $.get("/encheres/search", { filters: filters, category: category, openBids: openBids, myCurrentBids: myCurrentBids, wonBids: wonBids, currentSale: currentSale, salesNotStarted: salesNotStarted, completedSales: completedSales }, function(data) {
            // Met à jour la liste d'enchères avec les résultats de la recherche
            $(".listBidsContainer").html(data);
        });
    });
});