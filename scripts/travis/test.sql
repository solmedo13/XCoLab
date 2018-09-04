create table test1 (
  name varchar(50),
  string_value varchar(50)
);

create table test2 (
  user_id bigint,
  first_names varchar(50)
);

SET @isClimateColab = (select exists (select name from test1 where name = 'COLAB_NAME' and string_value = 'Climate CoLab'));

INSERT INTO test2 (user_id,first_names)
SELECT 2697976, "test"
WHERE @isClimateCoLab;
