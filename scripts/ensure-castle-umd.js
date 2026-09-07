const fs = require('fs');
const path = require('path');

const dist = path.join(__dirname, '..', 'node_modules', '@castleio', 'castle-js', 'dist');
const dest = path.join(dist, 'castle.umd.js');
if (!fs.existsSync(dist) || fs.existsSync(dest)) {
  process.exit(0);
}

const source = fs.readdirSync(dist).find((name) => (
  name.startsWith('castle.') && name.endsWith('.js') && name !== 'castle.js'
));
if (source) {
  fs.copyFileSync(path.join(dist, source), dest);
}
