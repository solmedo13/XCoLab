SET @isClimateColab = (select exists (select name from admin__configuration_attribute where name = 'COLAB_NAME' and string_value = 'Climate CoLab'));

INSERT INTO user__staff_member (id, user_id,category_id,first_names,last_name,role)
SELECT 342235232, 2697976, 5, "test", "test", "test"
WHERE @isClimateCoLab;
