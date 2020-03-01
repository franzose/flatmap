SELECT EXISTS (
   SELECT FROM pg_tables
   WHERE schemaname = 'public'
   AND tablename IN ('apartment', 'facility_type', 'facility', 'apartment_facility')
   HAVING COUNT(*) = 4
);
