# admin
# agent
# auth_dict
# auth_role
# auth_role_route
# auth_route
# banner
# country
# customer
# input_field
# pay_bank
# pay_card_type
# pay_channel
# pay_merchant
# payout_channel
# poster
# poster_category
# poster_code
# sys_address
# sys_msg_channel_merchant
# sys_msg_channel_template
# sys_multi_class_relation
# translation

-- 清理配置数据
update country set deleted=1 where lang_code not in ('zh-CN', 'en-US');
delete from pay_merchant where code != 'SYSTEMPAY';
delete from pay_channel where merchant_code != 'SYSTEMPAY';
delete from payout_channel where merchant_code != 'SYSTEMPAY';
delete from admin where username not in ('admin','developer');
delete from agent where username != 'aaaaaa';
delete from customer where agent_id != '' and agent_id is not null and agent_id != 1;
delete from pay_bank where merchant_code not in (select code from pay_merchant);
delete from pay_card_type where code !='BANK';

update admin set password = 'AAC1CB3291207102C7B06445D957A5F3' where username = 'admin';
update admin set password = 'B838D2ED848031243E6B4B5EBF2FD380' where username = 'developer'; -- developer2023
update agent set password = 'AAC1CB3291207102C7B06445D957A5F3';
update customer set number = '',username = '',subtitle = '',description = '',url = '';
# 清理 站点,域名/ip
UPDATE auth_dict
SET value = CASE
    WHEN value LIKE 'http://%' THEN
        CONCAT('http://127.0.0.1', SUBSTRING(value, LOCATE('/', value, 8)))
    WHEN value LIKE 'https://%' THEN
        CONCAT('http://127.0.0.1', SUBSTRING(value, LOCATE('/', value, 9)))
    ELSE value
END
where code in (
    'FILE_UPLOAD_DOMAIN',
    'FRONT_DOMAIN',
    'PAY_NOTIFY_URL',
    'PAY_URL',
    'APP_DOWNLOAD_URL',
    'ANDROID_DOWNLOAD_URL',
    'IOS_DOWNLOAD_URL'
    )
;
UPDATE auth_dict SET value = 'Site' where code ='SITE_NAME';
UPDATE auth_dict SET value = '0' where code ='SYSTEM_VERSION';
UPDATE auth_dict SET value = 'USA' where code ='SYSTEM_COUNTRY';
UPDATE auth_dict SET value = 'USD' where code ='SYSTEM_COUNTRY_CURRENCY';
UPDATE auth_dict SET value = '$' where code ='SYSTEM_COUNTRY_CURRENCY_SYMBOL';


--  清理用户数据
delete from sys_msg_record where user_id != 1;
delete from user_address where user_id != 1;
delete from user_bank_card where user_id != 1;
delete from user_money_record where user_id != 1;
delete from user_money_wait where user_id != 1;
delete from user_money_wait_record where user_id != 1;
delete from user_recharge where user_id != 1;
delete from user_transfer where user_id != 1;
delete from user_withdraw where user_id != 1;

