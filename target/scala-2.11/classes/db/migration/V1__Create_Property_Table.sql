CREATE TABLE "public"."property" (
	"id" varchar(255) NOT NULL,
	"broker_id" varchar(255) NOT NULL,
	"location_latitude" int4,
	"location_longitude" int4,
	"number_of_bathrooms" int NOT NULL,
	"number_of_bedrooms" int NOT NULL,
	"title" text,
	"price_amount" int NOT NULL,
	"price_currency" varchar(255) NOT NULL,
	"photo_url" varchar(255),
	PRIMARY KEY ("id") NOT DEFERRABLE INITIALLY IMMEDIATE
)
WITH (OIDS=FALSE);