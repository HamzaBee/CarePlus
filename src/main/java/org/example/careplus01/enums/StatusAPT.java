package org.example.careplus01.enums;

public enum StatusAPT {
    PLANNED,      // Prévu (par défaut à la création)
    WAITING,      // Le patient est arrivé en salle d'attente
    IN_PROGRESS,  // Le patient est avec le docteur
    FINISHED,     // La consultation est terminée
    CANCELED
}
