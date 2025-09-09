package ch.cag.recruiting.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

@Configuration
@EnableMongoAuditing
public class MongoConfig {
    
const { MongoClient, ServerApiVersion } = require('mongodb');

const credentials = '..\..\..\..\..\..\..\Cert\X509-cert-7494751078008559656.pem';

const client = new MongoClient('mongodb+srv://apde-cl01.o6n90y8.mongodb.net/?authSource=%24external&authMechanism=MONGODB-X509&retryWrites=true&w=majority&appName=APDE-cl01', {
  tlsCertificateKeyFile: credentials,
  serverApi: ServerApiVersion.v1
});

async function run() {
  try {
    await client.connect();
    const database = client.db("testDB");
    const collection = database.collection("testCol");
    const docCount = await collection.countDocuments({});
    console.log(docCount);
    // perform actions using client
  } finally {
    // Ensures that the client will close when you finish/error
    await client.close();
  }
}
run().catch(console.dir);

}
