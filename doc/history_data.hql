CREATE EXTERNAL TABLE `history_data`(
  key string,
  conid bigint,
  symbol string,
  currency string,
  secType  string,
  barSize string,
  duration bigint,
  durationUnit string,
  whatToHow string,
  time string,
  count bigint,
  low double,
  high double,
  open double,
  close double
  )
ROW FORMAT SERDE
  'org.apache.hadoop.hive.hbase.HBaseSerDe'
STORED BY
  'org.apache.hadoop.hive.hbase.HBaseStorageHandler'
WITH SERDEPROPERTIES (
  'hbase.columns.mapping'=':key,data:conid,data:symbol,data:currency,data:secType,data:barSize,data:duration,data:durationUnit,data:whatToHow,data:time,data:count,data:low,data:high,data:open,data:close',
  'serialization.format'='1')
TBLPROPERTIES (
  'hbase.table.name'='history_data');

