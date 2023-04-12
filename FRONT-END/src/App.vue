<template>
  <div id="app">
    <header><h1>Luca's Sharing Platform</h1></header>
    <router-view />
  </div>
</template>

<script>


export default {
  name: "App",
};
</script>


<style>
#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
}

header {
  padding: 6px;
  color: #fff;
  background-color: #343a40;
}
section > header {
  padding: 0px;
}

@media all and (max-width: 500px) {
  h1 {
    padding-left: 64px;
  }
}

h3 {
  padding: 15px;
  color: #fff;
  background-color: grey;
}

h4 {
  padding: 10px;
  background-color: beige;
}

form {
  padding: 10px;
  text-align: left;
  border:solid;
  border-width: 1px;
  display: flex;
  flex-direction: column;
  max-width: 500px;
  margin:  auto;
}


.btn {
  margin: 10px;
}
</style>
