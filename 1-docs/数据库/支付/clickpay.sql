-- 代收文档：https://business.clickpay.click/interface/index_inr.html
-- 代付文档：https://business.clickpay.click/interface/disbursement_inr.html
-- 商户编号：S820240616183245000006

-- 商户编号：S820240616183245000006
-- 商户私钥：MIICeAIBADANBgkqhkiG9w0BAQEFAASCAmIwggJeAgEAAoGBAPXpd7yVPl3DLCz+P8gFF7Gaw5DNCgrjUW1lJ7WOYaHU3R4WCjn5jzozETBNJMTsMXp5uDIgNz8kRVR4A0PahApteAFBUFBgrQWZ/E+jnBUluHlBC3M0/UuyQCMSyJQGrS48aFDUJDNJzexE3NiOnh2FxeDnxxGLwLN9E1iVR8KLAgMBAAECgYAcBKTC1NJNRo6C9dj4hjQD5VlA50usn+fIKg1TL0zKboFfCy3RGzEJdYYQ1clJsouUo8er1mXVCOHpVN8yXPRMLq1ZIPYcjDmjN7eSphooShUPcPJ+bc7A4nO3CnabX2L5IewNruOSwDeYa01jJBj8K2mCFgsB+x8pa/QAmoEvkQJBAP1Kg/xP13dN6xG3nna34IWxSKan0/VRyNqeYzLJS91YfgiWBqn+Vh+6KLFtnjHTvF5zZuV/vDM+6CQmwco9VVkCQQD4ir+8Z8mSHKVNcaBIzi6Dqb9cttEfMlO6kpQ5oMzgMNfw/sUTTkwde6hS0MBUE/p+Pl51tdSaq6vEhxa5bQaDAkEAw692GNB2Xq3MFnwN6RpWU/OLdXXjw3vT+VK0wN/TxMZBWNDPhTYBw7FHDwzpUwWKWk4+CMCO3eiT4JowzdZkwQJBAIqjctKYnlLXkcnCQSN7h2zAKlOAUuRxAU2IaBBBRycjd8EhjDL5sPGdQcZ4VWBRCKCVD8HfXKmuaxEkPaPPUhsCQQD62Rt5XRjk98k6blzPqJoHJi1VG1PnBv4adbyy90xnWoILCzd7r143/dezKFQEnNNsZ3Lv+ROkoe/MDfUYy8il
-- 平台公钥（代收付共用）：MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDKHWUUphTSd9EPvZwUWFe0TAT80zTiRdWbCZJVTe2Gtcouo6fh7JmZ1n6n2qm2xAutmrPxQ285ozL2TeXOF5H24lxi2lJx/gwBTMkcHYDNiXqtYpyKTkyIQC07dbjmStqfhNLR4vjYN9YHgbSSFBN90LqEevcxF2UiS+rTjx4MgQIDAQAB
-- 支付商户
insert into `pay_merchant`(code, name, mch_id, mch_key, public_key, plat_public_key,  payout_mch_id, payout_mch_key, api_url, withdraw_url, ht_url, enabled)
values('CLICKPAY_CLICKPAY', 'CLICKPAY支付',
       '商户号',
       '',
       '商户私钥',
       '平台公钥',
       '',
       '',
       'https://open.clickpay.click/gateway/pay',
       'https://open.clickpay.click/gateway/cash',
       '',
       1);

-- INSERT INTO `pay_merchant` ( `code`, `name`, `mch_id`, `public_key`, `plat_public_key`, `api_url`, `withdraw_url` )
-- VALUES
-- 	( 'CLICKPAY_CLICKPAY', 'CLICKPAY支付', 'S820240616183245000006', 'MIICeAIBADANBgkqhkiG9w0BAQEFAASCAmIwggJeAgEAAoGBAPXpd7yVPl3DLCz+\nP8gFF7Gaw5DNCgrjUW1lJ7WOYaHU3R4WCjn5jzozETBNJMTsMXp5uDIgNz8kRVR4\nA0PahApteAFBUFBgrQWZ/E+jnBUluHlBC3M0/UuyQCMSyJQGrS48aFDUJDNJzexE\n3NiOnh2FxeDnxxGLwLN9E1iVR8KLAgMBAAECgYAcBKTC1NJNRo6C9dj4hjQD5VlA\n50usn+fIKg1TL0zKboFfCy3RGzEJdYYQ1clJsouUo8er1mXVCOHpVN8yXPRMLq1Z\nIPYcjDmjN7eSphooShUPcPJ+bc7A4nO3CnabX2L5IewNruOSwDeYa01jJBj8K2mC\nFgsB+x8pa/QAmoEvkQJBAP1Kg/xP13dN6xG3nna34IWxSKan0/VRyNqeYzLJS91Y\nfgiWBqn+Vh+6KLFtnjHTvF5zZuV/vDM+6CQmwco9VVkCQQD4ir+8Z8mSHKVNcaBI\nzi6Dqb9cttEfMlO6kpQ5oMzgMNfw/sUTTkwde6hS0MBUE/p+Pl51tdSaq6vEhxa5\nbQaDAkEAw692GNB2Xq3MFnwN6RpWU/OLdXXjw3vT+VK0wN/TxMZBWNDPhTYBw7FH\nDwzpUwWKWk4+CMCO3eiT4JowzdZkwQJBAIqjctKYnlLXkcnCQSN7h2zAKlOAUuRx\nAU2IaBBBRycjd8EhjDL5sPGdQcZ4VWBRCKCVD8HfXKmuaxEkPaPPUhsCQQD62Rt5\nXRjk98k6blzPqJoHJi1VG1PnBv4adbyy90xnWoILCzd7r143/dezKFQEnNNsZ3Lv\n+ROkoe/MDfUYy8il', 'MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDKHWUUphTSd9EPvZwUWFe0TAT80zTiRdWbCZJVTe2Gtcouo6fh7JmZ1n6n2qm2xAutmrPxQ285ozL2TeXOF5H24lxi2lJx/gwBTMkcHYDNiXqtYpyKTkyIQC07dbjmStqfhNLR4vjYN9YHgbSSFBN90LqEevcxF2UiS+rTjx4MgQIDAQAB', 'https://open.clickpay.click/gateway/pay', 'https://open.clickpay.click/gateway/cash' );

-- 充值渠道 - 印度
insert into `pay_channel`(merchant_code, name, pay_type, min_amount, max_amount,  enabled, only_integer)
values
    ('CLICKPAY_CLICKPAY', 'UPI', 'UPI', 100, 50000000, 0, 1);

-- 代付渠道 - 印度
INSERT INTO `payout_channel` (`merchant_code`, `name`, `pay_type`, `min_amount`, `max_amount`, `sort`, `enabled`)
VALUES('CLICKPAY_CLICKPAY', 'CLICKPAY BANK', '0', '100', '500000000.00', '100', 0),
      ('CLICKPAY_CLICKPAY', 'CLICKPAY MERCHART', '1', '100', '500000000.00', '100', 0)
;





