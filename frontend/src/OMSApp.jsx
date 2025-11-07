import React, { useEffect, useMemo, useState } from "react";

/**
 * OMS Frontend – Pro UI (React + Tailwind)
 * - Polished layout (navbar, sections, cards, subtle animations)
 * - Create Order with live validation & total
 * - Orders list with status chips & actions
 * - Non-blocking toasts and loading states
 */
export default function OMSApp() {
  // If you use Vite proxy, set BASE_URL = ""; otherwise keep http://localhost:8080
  const BASE_URL = "http://localhost:8080";

  // -------- Helpers
  const currency = (v) => new Intl.NumberFormat("de-DE", { style: "currency", currency: "EUR" }).format(Number(v ?? 0));
  const newItem = () => ({ productId: "", quantity: 1, price: "0.00" });

  // -------- State
  const [orders, setOrders] = useState([]);
  const [loading, setLoading] = useState(false);
  const [submitting, setSubmitting] = useState(false);
  const [toast, setToast] = useState(null); // {type:"success|error", message}

  // Create form state
  const [customer, setCustomer] = useState({ customerId: "CUST-", prename: "", name: "" });
  const [address, setAddress] = useState({ street: "", city: "", zipCode: "", country: "Germany" });
  const [items, setItems] = useState([newItem()]);

  // Derived
  const totalAmount = useMemo(() => {
    try {
      const sum = items.map((it) => Number(it.price || 0) * Number(it.quantity || 0)).reduce((a, b) => a + b, 0);
      return sum.toFixed(2);
    } catch { return "0.00"; }
  }, [items]);

  const isFormValid = useMemo(() => {
    const hasCustomer = customer.customerId?.trim() && customer.prename?.trim() && customer.name?.trim();
    const hasAddress = address.street?.trim() && address.city?.trim() && address.zipCode?.trim() && address.country?.trim();
    const hasItems = items.length > 0 && items.every(i => i.productId?.trim() && Number(i.quantity) > 0 && Number(i.price) >= 0);
    return Boolean(hasCustomer && hasAddress && hasItems);
  }, [customer, address, items]);

  // -------- API
  async function api(path, options) {
    const res = await fetch(`${BASE_URL}${path}`, { headers: { "Content-Type": "application/json" }, ...options });
    if (!res.ok) throw new Error((await res.text()) || res.statusText);
    return res.status === 204 ? null : res.json();
  }

  async function loadOrders() {
    setLoading(true);
    try {
      const data = await api("/orders");
      setOrders(Array.isArray(data) ? data : []);
    } catch (e) { showToast("error", e.message); }
    finally { setLoading(false); }
  }

  async function createOrder() {
    if (!isFormValid || submitting) return;
    setSubmitting(true);
    const payload = {
      customer,
      items: items.map((it) => ({ productId: it.productId.trim(), quantity: Number(it.quantity), price: Number(it.price) })),
      totalAmount: Number(totalAmount),
      shippingAddress: address,
    };
    try {
      const created = await api("/orders", { method: "POST", body: JSON.stringify(payload) });
      setOrders((prev) => [created, ...prev]);
      resetForm();
      showToast("success", `Bestellung angelegt: ${created.orderId}`);
    } catch (e) { showToast("error", e.message); }
    finally { setSubmitting(false); }
  }

  async function cancelOrder(orderId) {
    try {
      const updated = await api(`/orders/${orderId}/cancel`, { method: "POST" });
      setOrders((prev) => prev.map((o) => (o.orderId === orderId ? updated : o)));
      showToast("success", `Bestellung storniert: ${orderId}`);
    } catch (e) { showToast("error", e.message); }
  }

  function resetForm() {
    setCustomer({ customerId: "CUST-", prename: "", name: "" });
    setAddress({ street: "", city: "", zipCode: "", country: "Germany" });
    setItems([newItem()]);
  }

  function showToast(type, message) {
    setToast({ type, message });
    window.clearTimeout(showToast._t);
    showToast._t = window.setTimeout(() => setToast(null), 2800);
  }

  useEffect(() => { loadOrders(); }, []);

  // -------- Small UI components
  const StatusChip = ({ status }) => {
    const map = {
      CREATED: "bg-slate-100 text-slate-800",
      RESERVED: "bg-blue-100 text-blue-800",
      PAID: "bg-emerald-100 text-emerald-800",
      PACKED: "bg-amber-100 text-amber-800",
      SHIPPED: "bg-indigo-100 text-indigo-800",
      DELIVERED: "bg-green-100 text-green-800",
      FAILED: "bg-rose-100 text-rose-800",
      CANCELLED: "bg-slate-200 text-slate-700",
    };
    const cls = map[status] || map.CREATED;
    return <span className={`inline-flex items-center rounded-full px-2 py-0.5 text-[11px] font-medium ${cls}`}>{status}</span>;
  };

  // -------- Render
  return (
    <div className="min-h-screen bg-gradient-to-br from-sky-50 via-indigo-50 to-purple-50">
      {/* Top Bar */}
      <div className="sticky top-0 z-20 bg-gradient-to-r from-indigo-600 via-violet-600 to-fuchsia-600 text-white shadow">
        <div className="mx-auto max-w-7xl px-5 py-3 flex items-center justify-between">
          <div className="flex items-center gap-3">
            <div className="h-8 w-8 rounded-xl bg-blue-600 text-white grid place-items-center font-bold">O</div>
            <div>
              <div className="text-[15px] font-semibold leading-none">OMS Dashboard</div>
              <div className="text-[12px] text-slate-500 leading-none">Order Management • Team 6</div>
            </div>
          </div>
          <div className="flex items-center gap-2">
            <button onClick={loadOrders} className="px-3 py-1.5 rounded-lg bg-white/15 hover:bg-white/25 text-white shadow-sm text-sm transition">Refresh</button>
            <a href="/swagger-ui.html" className="px-3 py-1.5 rounded-lg bg-white text-indigo-700 text-sm font-medium shadow hover:bg-slate-100 transition">Swagger</a>
          </div>
        </div>
      </div>

      {/* Page Body */}
      <div className="mx-auto max-w-7xl px-5 py-6 grid grid-cols-1 lg:grid-cols-3 gap-6">
        {/* Create Card */}
        <section className="lg:col-span-2 bg-white/95 backdrop-blur rounded-2xl shadow ring-1 ring-indigo-100 p-6">
          <div className="flex items-center justify-between mb-4">
            <h2 className="text-lg font-semibold">Neue Bestellung</h2>
            <span className="inline-flex items-center rounded-full bg-indigo-100 text-indigo-800 px-3 py-1 text-xs font-medium">Total: {currency(totalAmount)}</span>
          </div>

          {/* Form */}
          <form onSubmit={(e)=>{ e.preventDefault(); createOrder(); }}>
            <div className="grid grid-cols-1 md:grid-cols-3 gap-5">
              {/* Customer */}
              <div className="space-y-2">
                <h3 className="text-sm font-medium text-slate-700">Kunde</h3>
                <Input placeholder="Customer ID" value={customer.customerId} onChange={(v)=>setCustomer({...customer, customerId:v})}/>
                <Input placeholder="Vorname" value={customer.prename} onChange={(v)=>setCustomer({...customer, prename:v})}/>
                <Input placeholder="Nachname" value={customer.name} onChange={(v)=>setCustomer({...customer, name:v})}/>
              </div>

              {/* Address */}
              <div className="space-y-2">
                <h3 className="text-sm font-medium text-slate-700">Adresse</h3>
                <Input placeholder="Straße" value={address.street} onChange={(v)=>setAddress({...address, street:v})}/>
                <div className="grid grid-cols-2 gap-2">
                  <Input placeholder="PLZ" value={address.zipCode} onChange={(v)=>setAddress({...address, zipCode:v})}/>
                  <Input placeholder="Ort" value={address.city} onChange={(v)=>setAddress({...address, city:v})}/>
                </div>
                <Input placeholder="Land" value={address.country} onChange={(v)=>setAddress({...address, country:v})}/>
              </div>

              {/* Items */}
              <div className="space-y-2 md:col-span-1">
                <h3 className="text-sm font-medium text-slate-700">Positionen</h3>
                <div className="space-y-2">
                  {items.map((it, idx) => (
                    <div key={idx} className="rounded-xl border border-indigo-100 p-3 bg-indigo-50/40">
                      <div className="grid grid-cols-12 gap-2">
                        <Input className="col-span-5" placeholder="Produkt-ID" value={it.productId} onChange={(v)=>updateItem(idx,{...it, productId:v})}/>
                        <Input type="number" min={1} className="col-span-3" placeholder="Menge" value={it.quantity} onChange={(v)=>updateItem(idx,{...it, quantity:v})}/>
                        <Input type="number" step="0.01" className="col-span-4" placeholder="Preis" value={it.price} onChange={(v)=>updateItem(idx,{...it, price:v})}/>
                      </div>
                      <div className="mt-2 flex justify-between text-xs text-slate-500">
                        <span>Zwischensumme</span>
                        <span className="font-medium">{currency(Number(it.price||0)*Number(it.quantity||0))}</span>
                      </div>
                      <div className="mt-2 text-right">
                        <button className="text-[12px] text-rose-600 hover:underline" onClick={()=> removeItem(idx)}>Entfernen</button>
                      </div>
                    </div>
                  ))}
                </div>
                <div className="flex justify-between items-center">
                  <button className="text-sm text-blue-600 hover:underline" onClick={()=> setItems([...items, newItem()])}>+ Position</button>
                  <div className="text-sm">Summe: <span className="font-semibold">{currency(totalAmount)}</span></div>
                </div>
              </div>
            </div>

            <div className="mt-5 flex flex-wrap items-center gap-3">
              <button
                type="submit"
                onClick={createOrder}
                disabled={!isFormValid || submitting}
                className={`inline-flex items-center gap-2 rounded-xl px-4 py-2 text-white shadow transition bg-gradient-to-r from-indigo-600 to-fuchsia-600 hover:from-indigo-700 hover:to-fuchsia-700 ${(!isFormValid || submitting) ? 'opacity-60 cursor-not-allowed' : ''}`}
              >
                {submitting ? <Spinner/> : null}
                Bestellung anlegen
              </button>
              <button onClick={resetForm} className="rounded-xl border px-4 py-2 hover:bg-slate-50">Zurücksetzen</button>
            </div>
          </form>
        </section>

        {/* Right column – Orders */}
        <section className="lg:col-span-1 space-y-4">
          <div className="bg-white/90 backdrop-blur rounded-2xl shadow ring-1 ring-indigo-100 p-5">
            <div className="flex items-center justify-between mb-3">
              <h3 className="text-sm font-semibold">Bestellungen</h3>
              {loading ? <Spinner/> : <button onClick={loadOrders} className="text-sm text-slate-600 hover:underline">Aktualisieren</button>}
            </div>

            {orders.length === 0 ? (
              <EmptyState/>
            ) : (
              <ul className="divide-y divide-slate-100">
                {orders.map((o) => (
                  <li key={o.orderId} className="py-3 flex items-start justify-between gap-3">
                    <div className="space-y-1">
                      <div className="flex items-center gap-2">
                        <span className="font-mono text-xs md:text-sm">{o.orderId}</span>
                        <StatusChip status={o.status} />
                      </div>
                      <div className="text-[12px] text-slate-600">
                        {o?.customer?.prename} {o?.customer?.name} • {o?.shippingAddress?.city}
                      </div>
                      <div className="text-[12px] text-slate-500">{o.items?.length ?? 0} Position(en) • {currency(o.totalAmount)}</div>
                    </div>
                    <div className="flex items-center gap-2">
                      <button
                        className="rounded-lg border px-3 py-1.5 text-xs md:text-sm hover:bg-slate-50"
                        onClick={() => cancelOrder(o.orderId)}
                        disabled={o.status === "CANCELLED" || o.status === "DELIVERED" || o.status === "SHIPPED"}
                      >Stornieren</button>
                    </div>
                  </li>
                ))}
              </ul>
            )}
          </div>
        </section>
      </div>

      {/* Toast */}
      {toast && (
        <div className="fixed bottom-4 right-4 z-30">
          <div className={`rounded-xl px-4 py-3 shadow-lg text-sm text-white ${toast.type==='success'?'bg-emerald-600':'bg-rose-600'}`}>
            {toast.message}
          </div>
        </div>
      )}

      {/* Local styles */}
      <style>{`
        .inputBase { @apply w-full rounded-xl border border-slate-200 bg-white px-3 py-2 text-sm shadow-sm outline-none transition focus:ring-2 focus:ring-fuchsia-500/50 focus:border-fuchsia-500 placeholder-slate-400; }
      `}</style>
    </div>
  );

  // -------- Local components & handlers
  function Input({ className = "", type = "text", value, onChange, placeholder, min, step }) {
    const val = typeof value === 'string' ? value : (value ?? '');
    return (
      <input
        className={`inputBase ${className}`}
        type={type}
        value={val}
        placeholder={placeholder}
        min={min}
        step={step}
        onChange={(e) => onChange?.(e.target.value)}
      />
    );
  }

  function Spinner() {
    return (
      <svg className="h-4 w-4 animate-spin text-slate-600" viewBox="0 0 24 24">
        <circle className="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" strokeWidth="4" fill="none"/>
        <path className="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8v4a4 4 0 00-4 4H4z"/>
      </svg>
    );
  }

  function EmptyState() {
    return (
      <div className="text-center py-10">
        <div className="mx-auto h-10 w-10 rounded-full bg-slate-100 grid place-items-center text-slate-500">🧾</div>
        <div className="mt-2 text-sm text-slate-600">Noch keine Bestellungen.</div>
        <div className="text-xs text-slate-500">Lege links eine neue Bestellung an.</div>
      </div>
    );
  }

  function updateItem(idx, next) { setItems((prev) => prev.map((it, i) => (i === idx ? next : it))); }
  function removeItem(idx) { setItems((prev) => (prev.length > 1 ? prev.filter((_, i) => i !== idx) : [newItem()])); }
}
