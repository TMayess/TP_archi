# Design Pattern — State Pattern

## Pattern choisi : State (Comportemental)

## Justification

Le cycle de vie d'une réservation suit des transitions d'états bien définies :
- CONFIRMED -> CANCELLED (annulation)
- CONFIRMED -> COMPLETED (complétion)
- CANCELLED -> rien (état final)
- COMPLETED -> rien (état final)

Le **State Pattern** permet d'encapsuler le comportement associé à chaque état
dans une classe dédiée, évitant les multiples `if/else` sur le statut.

## Structure

- `ReservationState` - interface définissant les transitions possibles
- `ConfirmedState` - autorise cancel() et complete()
- `CancelledState` - lève une exception sur toute transition
- `CompletedState` - lève une exception sur toute transition

## Avantages

- Chaque état gère ses propres règles de transition
- Facile d'ajouter un nouvel état sans modifier le code existant
- Respecte le principe Open/Closed (SOLID)