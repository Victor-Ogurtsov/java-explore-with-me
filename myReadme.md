# java-explore-with-me

### Template repository for ExploreWithMe project.

В третьей части ДП добавлены сущности Comment (комментарий к событию) и ComplaintComment (жалоба на комментарий).

### Эндпоинты API

Приватные:
#### POST /users/{userId}/comments?eventId={eventId}

Зарегистрированные пользователи могут оставлять комментарии к событиям.

#### PATCH /users/{userId}/comments/{commentId}

В течение 15 минут пользователь может отредактировать свой комментарий.

Публичные:

#### GET /comments/{eventId}

Комментарии к событию доступны всем пользователям ресурса.

#### POST /complaints/{commentId}

Любой человек может оставить жалобу на недопустимый комментарий, которая будет рассмотрена администраторами.

Администраторы:

#### PATCH /admin/complaint/{complaintId}?status=CONFIRMED

Администраторы обрабатывают жалобу и присваивают ей статус CONFIRMED или REJECTED

#### PATCH /admin/comments/{commentId}/canceled

Если жалоба подтверждена администратором, он меняет статус комментария  на CANCELED и тот больше не отображается среди комментариев к событию.