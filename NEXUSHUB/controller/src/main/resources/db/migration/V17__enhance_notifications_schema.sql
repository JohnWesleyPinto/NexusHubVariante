-- V17: Enhance usr_notification table for modular notifications (title, type, link, email flags)
ALTER TABLE usr_notification ADD COLUMN IF NOT EXISTS dstitle VARCHAR(150);
ALTER TABLE usr_notification ADD COLUMN IF NOT EXISTS dstype VARCHAR(50) DEFAULT 'SYSTEM_NOTICE';
ALTER TABLE usr_notification ADD COLUMN IF NOT EXISTS dslink VARCHAR(255);
ALTER TABLE usr_notification ADD COLUMN IF NOT EXISTS flsendemail BOOLEAN NOT NULL DEFAULT FALSE;
ALTER TABLE usr_notification ADD COLUMN IF NOT EXISTS flemailsent BOOLEAN NOT NULL DEFAULT FALSE;

CREATE INDEX IF NOT EXISTS ix_usr_notification_type ON usr_notification (dstype);
CREATE INDEX IF NOT EXISTS ix_usr_notification_read ON usr_notification (idreceiver, flread);
