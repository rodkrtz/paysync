CREATE TABLE public.conta (
	id serial PRIMARY KEY,
	data_vencimento TIMESTAMP(0) WITHOUT TIME ZONE NOT NULL,
	data_pagamento TIMESTAMP(0) WITHOUT TIME ZONE NULL,
	data_cadastro TIMESTAMP(0) WITHOUT TIME ZONE NOT NULL,
	data_atualizacao TIMESTAMP(0) WITHOUT TIME ZONE NOT NULL,
	valor numeric NOT NULL,
	descricao varchar NULL,
	situacao varchar NOT NULL
);