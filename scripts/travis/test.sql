create table admin__configuration_attribute (
  name varchar(50),
  string_value varchar(50)
);

create table user__staff_member (
  user_id bigint,
  first_names varchar(50)
);

SET @isClimateColab = (select exists (select name from admin__configuration_attribute where name = 'COLAB_NAME' and string_value = 'Climate CoLab'));

INSERT INTO user__staff_member (user_id,first_names)
SELECT 2697976, "test"
WHERE @isClimateCoLab;
