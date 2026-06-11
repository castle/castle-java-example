// Lightweight helpers shared across the Castle demo pages (no jQuery).

async function postJSON(url, data) {
  const res = await fetch(url, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(data || {}),
  });
  let body;
  try {
    body = await res.json();
  } catch (e) {
    body = { error: "Server returned a non-JSON response (status " + res.status + ")." };
  }
  return body;
}

// Resolve a Castle request token, falling back gracefully if the browser SDK
// is unavailable (e.g. no publishable key configured).
function withRequestToken(callback) {
  if (window.Castle && typeof Castle.createRequestToken === "function") {
    Castle.createRequestToken()
      .then(callback)
      .catch(function (err) {
        console.error("Castle.createRequestToken failed", err);
        callback("");
      });
  } else {
    callback("");
  }
}

function syntaxHighlight(obj) {
  let json = JSON.stringify(obj, null, 2);
  json = json.replace(/&/g, "&amp;").replace(/</g, "&lt;").replace(/>/g, "&gt;");
  return json.replace(
    /("(\\u[a-zA-Z0-9]{4}|\\[^u]|[^\\"])*"(\s*:)?|\b(true|false)\b|\bnull\b|-?\d+(?:\.\d*)?(?:[eE][+\-]?\d+)?)/g,
    function (match) {
      let cls = "n";
      if (/^"/.test(match)) {
        cls = /:$/.test(match) ? "k" : "s";
      } else if (/true|false/.test(match)) {
        cls = "b";
      } else if (/null/.test(match)) {
        cls = "z";
      }
      return '<span class="' + cls + '">' + match + "</span>";
    },
  );
}

function clearResults() {
  const el = document.getElementById("results");
  if (el) el.innerHTML = "";
}

function addEndpointBadge(endpoint) {
  const el = document.getElementById("results");
  if (!el) return;
  const wrap = document.createElement("div");
  wrap.className = "result-block";
  wrap.innerHTML =
    '<div class="label">Castle endpoint</div><span class="badge endpoint">/' + endpoint + "</span>";
  el.appendChild(wrap);
}

function addJSONBlock(label, value) {
  const el = document.getElementById("results");
  if (!el) return;
  const wrap = document.createElement("div");
  wrap.className = "result-block";
  const lbl = document.createElement("div");
  lbl.className = "label";
  lbl.textContent = label;
  const pre = document.createElement("pre");
  pre.className = "json";
  pre.innerHTML = syntaxHighlight(value);
  wrap.appendChild(lbl);
  wrap.appendChild(pre);
  el.appendChild(wrap);
}

function showResultsCard() {
  const card = document.getElementById("results-card");
  if (card) card.classList.remove("hidden");
}

// Render the headline verdict (allow / challenge / deny) plus the risk score
// and any signals returned by the risk/filter endpoints.
function addVerdictBanner(result) {
  const el = document.getElementById("results");
  if (!el || !result || typeof result !== "object") return;

  const action = result.policy && result.policy.action;
  const hasScore = typeof result.risk === "number";
  if (!action && !hasScore) return;

  const wrap = document.createElement("div");
  wrap.className = "result-block";

  const banner = document.createElement("div");
  banner.className = "verdict verdict-" + (action || "unknown");

  let html = "";
  if (action) {
    html += '<span class="verdict-action">' + action + "</span>";
  }
  if (hasScore) {
    html +=
      '<span class="verdict-score">risk <strong>' +
      result.risk.toFixed(2) +
      "</strong></span>";
  }
  banner.innerHTML = html;
  wrap.appendChild(banner);

  const signals = result.signals && Object.keys(result.signals);
  if (signals && signals.length) {
    const chips = document.createElement("div");
    chips.className = "signals";
    signals.forEach(function (name) {
      const chip = document.createElement("span");
      chip.className = "chip";
      chip.textContent = name;
      chips.appendChild(chip);
    });
    wrap.appendChild(chips);
  }

  el.appendChild(wrap);
}

// Standard renderer for the {api_endpoint, payload_to_castle, result} shape
// returned by the demo backend routes.
function renderCastleResponse(data) {
  clearResults();
  if (data.api_endpoint) addEndpointBadge(data.api_endpoint);
  addVerdictBanner(data.result);
  if (data.payload_to_castle) addJSONBlock("Payload sent to Castle", data.payload_to_castle);
  if (data.result !== undefined && data.result !== null) {
    addJSONBlock("Response from Castle", data.result);
  }
  showResultsCard();
}

// Renders an ordered sequence of Castle calls (e.g. the login Filter -> Risk
// flow), one endpoint/verdict/payload/result block per step.
function renderCastleSteps(steps) {
  clearResults();
  (steps || []).forEach(function (step) {
    if (step.api_endpoint) addEndpointBadge(step.api_endpoint);
    addVerdictBanner(step.result);
    if (step.payload_to_castle) addJSONBlock("Payload sent to Castle", step.payload_to_castle);
    if (step.result !== undefined && step.result !== null) {
      addJSONBlock("Response from Castle", step.result);
    }
  });
  showResultsCard();
}

// Tell Castle which page the user is on. Safe no-op if the browser SDK or the
// publishable key is unavailable.
function trackPage() {
  if (window.Castle && typeof Castle.page === "function") {
    try {
      Castle.page();
    } catch (e) {
      console.error("Castle.page failed", e);
    }
  }
}

// Fire an ad-hoc client-side event (e.g. a button click) to Castle.
function trackCustomEvent(name) {
  if (window.Castle && typeof Castle.custom === "function") {
    try {
      Castle.custom({ name: name });
    } catch (e) {
      console.error("Castle.custom failed", e);
    }
  }
}

document.addEventListener("DOMContentLoaded", trackPage);
