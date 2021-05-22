insert into tipo_corredor(id,tipo)
values
(null,'2km'),
(null,'100 metros');

insert into tipo_nadador(id,tipo)
values
(null,'borboleta'),
(null,'peito');

insert into atleta(id,nome_atleta,tipo_dieta,treino_por_dia, tipo_corredor_id, tipo_nadador_id)
values
(123,'jose','lowcarb',2,1,2),
(456,'paulo','highcarb',5,1,1),
(789,'henrique','diety',6,2,2);
