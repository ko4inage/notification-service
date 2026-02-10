Микросервис 1: Request Processor

Имеет один контроллер для обработки Endpoint: /api/v1/notifications
Method:	POST
Request Body:
{
"type": "(Enum) SMS|EMAIL|PUSH|TG_MESSAGE",
"message": "string"
}

Паттерна "Стратегия" используется в Enum-классе для выбора обработчика по типу уведомления (в зависимости от значения поля type).

Обработчики подготавливают сообщения, которые отправляются в соответствующий топик Kafka.

Для гарантированной доставки использован паттерн Transactional Outbox. Каждый обработчик:

Генерирует UUID ключ для сообщения (для каждого сообщения новый уникальный ключ)

Формирует тело сообщения в JSON

Определяет целевой топик Kafka (для sms - sms-events, для email - email-events, для push - push-events, для TG_MESSAGE - telegram-events)

Сохраняет сообщение в таблицу notification_outbox через Transactional Outbox

Логирует: "Подготовлено сообщение для отправки. Key: <key>, Payload: <body>, topic: <topic>".

Шедулер с заданным настраиваемым интервалом периодически выбирает неотправленные сообщения и отправляет их в Kafka.

Выбирает до N (batch-size из конфига) сообщений (where sent = false, сортировка по created_at asc)

Отправляет в Kafka

При успехе обновляет флаг sent = true

При ошибке увеличивает счетчик attempt и пропускает запись

